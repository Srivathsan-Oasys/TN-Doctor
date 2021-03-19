package com.oasys.digihealth.doctor.ui.configuration.view


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.ActivityConfigBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.ui.configuration.model.ConfigResponseContent
import com.oasys.digihealth.doctor.ui.configuration.model.ConfigResponseModel
import com.oasys.digihealth.doctor.ui.configuration.model.ConfigUpdateRequestModel
import com.oasys.digihealth.doctor.ui.configuration.model.ConfigUpdateResponseModel
import com.oasys.digihealth.doctor.ui.configuration.view_model.ConfigViewModel
import com.oasys.digihealth.doctor.ui.configuration.view_model.ConfigViewModelFactory
import com.oasys.digihealth.doctor.ui.dashboard.view.DashBoardActivity
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EmrWorkFlowResponseModel
import com.oasys.digihealth.doctor.ui.home.HomeActivity
import com.oasys.digihealth.doctor.utils.CustomProgressDialog
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response


class ConfigActivity : Fragment(), StartDragListener {
    private var utils: Utils? = null
    private var conficFinalData: ArrayList<ConfigResponseContent?>? = ArrayList()
    private var binding: ActivityConfigBinding? = null
    private var viewModel: ConfigViewModel? = null
    private var configadapter: ConfigRecyclerAdapter? = null
    private var configfavadapter: ConfigFavRecyclerAdapter? = null
    internal var touchHelper: ItemTouchHelper? = null
    private var customProgressDialog: CustomProgressDialog? = null

    var requestparameter: ConfigUpdateRequestModel? = ConfigUpdateRequestModel()
    private var configRequestData: ArrayList<ConfigUpdateRequestModel?>? = ArrayList()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var appPreferences: AppPreferences? = null
    var selecteConfriguration: Int? = null
    var configStatus: Boolean = false

    val flow: String?
        get() = if (arguments == null) null else requireArguments().getSerializable(ConfigActivity.ARG_NAME) as String

    //private var customProgressDialog: CustomProgressDialog? = null
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.activity_config,
                container,
                false
            )
        viewModel = ConfigViewModelFactory(
            requireActivity().application
        ).create(ConfigViewModel::class.java)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        utils = Utils(requireContext())
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        checkForFlow()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val DepartmentID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        val facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        customProgressDialog = CustomProgressDialog(context)

        setupSpinner()

        configadapter = ConfigRecyclerAdapter(context, ArrayList())
        binding!!.ConfigRecyclerView.adapter = configadapter
        val searchText =
            binding?.searchView?.findViewById(R.id.search_src_text) as TextView
        val tf = ResourcesCompat.getFont(requireContext(), R.font.poppins)
        searchText.typeface = tf
        binding?.searchView?.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            @SuppressLint("RestrictedApi")
            override fun onQueryTextSubmit(query: String): Boolean {
                callSearch(query)
                binding?.searchView?.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                callSearch(newText)
                return true
            }

            fun callSearch(query: String) {
                configadapter?.getFilter()?.filter(query)
            }
        })
        trackConfigurationAnalyticsVisit()

        configfavadapter = ConfigFavRecyclerAdapter(this, ArrayList())
        val callback = ItemMoveCallback(configfavadapter)
        touchHelper = ItemTouchHelper(callback)
        touchHelper!!.attachToRecyclerView(binding?.ConfigfavRecyclerView)
        binding!!.ConfigfavRecyclerView.adapter = configfavadapter
        binding!!.clearCardView!!.setOnClickListener {
            conficFinalData = configfavadapter?.getFinalData()

            if (conficFinalData?.size == 0) {
                Toast.makeText(requireContext(), "All item moved already", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            conficFinalData = configfavadapter?.getFinalData()
            for (i in conficFinalData!!.indices) {
                configadapter?.setConfigfavList(conficFinalData!!.get(i))
            }
            configfavadapter?.clearALL()
            binding?.conficount?.text = configadapter?.getItemSize().toString()
            binding?.confifavcount?.text = configfavadapter?.getItemSize().toString()

        }
        binding!!.click.setOnClickListener {
            conficFinalData = configfavadapter?.getFinalData()
            if (conficFinalData?.size!! > 0) {
                for (i in conficFinalData?.indices!!) {
                    val configData: ConfigUpdateRequestModel = ConfigUpdateRequestModel()
                    configData.facility_uuid = facility_id
                    configData.department_uuid = DepartmentID
                    configData.work_flow_order = i + 1
                    configData.user_uuid = userDataStoreBean?.uuid
                    configData.role_uuid = 1
                    configData.status = true
                    configData.revision = 1
                    configData.context_uuid = conficFinalData?.get(i)?.context_uuid
                    configData.context_activity_map_uuid = conficFinalData?.get(i)!!.uuid
                    configData.activity_uuid = conficFinalData?.get(i)?.activity_uuid

                    configRequestData!!.add(configData)
                }
                viewModel?.postRequestParameter(
                    facility_id,
                    configRequestData!!,
                    configFinalRetrofitCallBack,
                    configStatus
                )

                if (selecteConfriguration == 2) {
                    trackConfigSaveAnalyticsVisit("OP")
                } else {
                    trackConfigSaveAnalyticsVisit("IP")
                }

            } else {
                utils!!.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Please add any one item"
                )
            }
        }
        binding?.moveall!!.setOnClickListener {

            conficFinalData = configadapter?.getConfigData()
            if (conficFinalData?.size == 0) {
                Toast.makeText(requireContext(), "All item moved already", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val conficfev = configfavadapter?.getFinalData()
            val confignormal = configadapter?.getConfigData()
            for (i in confignormal!!.indices) {
                val check =
                    conficfev!!.any { it!!.activity_uuid == confignormal.get(i)!!.activity_uuid }
                if (!check) {
                    configfavadapter?.setConfigfavList(confignormal.get(i)!!)

                }
            }
//            configadapter?.removeall(conficFinalData)
            binding?.conficount?.text = configadapter?.getItemSize().toString()
            binding?.confifavcount?.text = configfavadapter?.getItemSize().toString()

        }
        configadapter!!.setOnItemClickListener(object :
            ConfigRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(
                configResponseContent: ConfigResponseContent,
                position: Int
            ) {

                conficFinalData = configfavadapter?.getFinalData()
                val check =
                    conficFinalData!!.any { it!!.activity_uuid == configResponseContent.activity_uuid }
                if (!check) {
                    configfavadapter?.setConfigfavList(configResponseContent)
                    binding?.confifavcount?.text = configfavadapter?.getItemSize().toString()
                    configadapter?.removeitem(position)
                    binding?.conficount?.text = configadapter?.getItemSize().toString()
                } else {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        "Already Item is available"
                    )
                }
            }
        })
        configfavadapter!!.setOnItemClickListener(object :
            ConfigFavRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(
                configfavResponseContent: ConfigResponseContent?,
                position: Int
            ) {

                configadapter?.setConfigList(configfavResponseContent)
                configfavadapter?.removeitem(position)

                binding?.conficount?.text = configadapter?.getItemSize().toString()

                binding?.confifavcount?.text = configfavadapter?.getItemSize().toString()
            }
        })
        return binding!!.root
    }

    val configRetrofitCallBack = object : RetrofitCallback<ConfigResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<ConfigResponseModel?>) {
            configadapter?.clearALL()
            configadapter?.setConfigItem(responseBody.body()?.responseContents!!)

            binding?.conficount?.text = responseBody.body()?.responseContents!!.size.toString()
            viewModel?.getEmrWorkFlowFav(emrWorkFlowRetrofitCallBack, selecteConfriguration)

        }

        override fun onBadRequest(errorBody: Response<ConfigResponseModel?>) {
        }

        override fun onServerError(response: Response<*>?) {

        }

        override fun onUnAuthorized() {

        }

        override fun onForbidden() {

        }

        override fun onFailure(s: String?) {

        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    private val configFinalRetrofitCallBack = object : RetrofitCallback<ConfigUpdateResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<ConfigUpdateResponseModel?>) {
            utils!!.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                responseBody.body()?.message!!
            )


            val emr = DashBoardActivity()

            (activity as HomeActivity).replaceFragment(emr)
//            startActivity(Intent(context, DashBoardActivity::class.java))

        }

        override fun onBadRequest(errorBody: Response<ConfigUpdateResponseModel?>) {
        }

        override fun onServerError(response: Response<*>?) {

        }

        override fun onUnAuthorized() {

        }

        override fun onForbidden() {

        }

        override fun onFailure(s: String?) {

        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }
/*

    private val configCreateRetrofitCallBack = object : RetrofitCallback<ConfigUpdateResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<ConfigUpdateResponseModel>?) {
            utils!!.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                responseBody?.body()?.message!!
            )


            val emr= DashBoardActivity()

            (activity as MainLandScreenActivity).replaceFragment(emr)
//            startActivity(Intent(context, DashBoardActivity::class.java))

        }

        override fun onBadRequest(errorBody: Response<ConfigUpdateResponseModel>?) {
        }

        override fun onServerError(response: Response<*>?) {

        }

        override fun onUnAuthorized() {

        }

        override fun onForbidden() {

        }

        override fun onFailure(s: String?) {

        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }
*/

    override fun requestDrag(viewHolder: RecyclerView.ViewHolder) {
        touchHelper?.startDrag(viewHolder)
    }

    private val emrWorkFlowRetrofitCallBack = object : RetrofitCallback<EmrWorkFlowResponseModel> {
        override fun onSuccessfulResponse(response: Response<EmrWorkFlowResponseModel?>) {
            if (response.body()?.responseContents?.isNotEmpty()!!) {
                /* for(i in response?.body()?.responseContents!!.indices){
                     configadapter?.setConfigItemRemove(response.body()?.responseContents?.get(i))
                 }*/
                configfavadapter?.clearALL()
                configfavadapter?.setConfigfavarrayList(response.body()?.responseContents!!)
                binding?.confifavcount?.text = configfavadapter?.getItemSize().toString()

                configStatus = false

            } else {
                configStatus = true

            }
        }

        override fun onBadRequest(response: Response<EmrWorkFlowResponseModel?>) {
            val gson = GsonBuilder().create()
            val responseModel: EmrWorkFlowResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    EmrWorkFlowResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.message!!
                )
            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String?) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure ?: "")
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    fun setupSpinner() {
        val op = "EMR OP"
        val ip = "EMR IP"
        val categories = ArrayList<String>()
        categories.add(op)
        categories.add(ip)
        val adapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_item, categories)
        binding?.chooseConfig?.adapter = adapter
        binding?.chooseConfig?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    if (pos == 0) {
                        selecteConfriguration = 2
                        viewModel?.getConfigList(selecteConfriguration, configRetrofitCallBack)
                    } else if (pos == 1) {
                        selecteConfriguration = 3
                        viewModel?.getConfigList(selecteConfriguration, configRetrofitCallBack)
                    }
                    setupViemodelProgress()
                }

            }
    }

    fun setupViemodelProgress() {
        viewModel!!.progress.observe(viewLifecycleOwner,
            Observer { progress ->
                if (progress == View.VISIBLE) {
                    customProgressDialog!!.show()
                } else if (progress == View.GONE) {
                    customProgressDialog!!.dismiss()
                }
            })
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
    }

    fun checkForFlow() {
        if (flow.equals(AppConstants.CONFRIGURATION)) {
            binding?.chooseConfig?.visibility = View.VISIBLE
        } else if (flow.equals(AppConstants.OUT_PATIENT)) {
            binding?.chooseConfig?.visibility = View.GONE
            selecteConfriguration = 2
            viewModel?.getConfigList(selecteConfriguration, configRetrofitCallBack)
        } else {
            binding?.chooseConfig?.visibility = View.GONE
            selecteConfriguration = 3
            viewModel?.getConfigList(selecteConfriguration, configRetrofitCallBack)
        }
    }

    companion object {
        const val ARG_NAME = "from"
        fun newInstance(from: String, Status: Boolean): ConfigActivity {
            val fragment = ConfigActivity()
            val bundle = Bundle().apply {
                putSerializable(ARG_NAME, from)
                putSerializable("Status", Status)
            }
            fragment.arguments = bundle
            return fragment
        }
    }


    fun trackConfigurationAnalyticsVisit() {
        AnalyticsManager.getAnalyticsManager().trackConfigurationVisit()
    }

    fun trackConfigSaveAnalyticsVisit(type: String) {
        AnalyticsManager.getAnalyticsManager().trackOPConfigSave(type)
    }
}