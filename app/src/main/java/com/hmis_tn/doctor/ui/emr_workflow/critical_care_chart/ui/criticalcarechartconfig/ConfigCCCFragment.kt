package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.ui.criticalcarechartconfig

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.FragmentHisoryConfigBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.configuration.model.*
import com.hmis_tn.doctor.ui.configuration.view.StartDragListener
import com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.config.CriticalCareChartConfigListData
import com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.config.CriticalCareChartFilterHeadingsResponse
import com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.config.SaveCriticalCareChartConfig
import com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.headings.GetCriticalCareChartHeadingsReq
import com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.headings.GetCriticalCareChartHeadingsResp
import com.hmis_tn.doctor.ui.emr_workflow.history.config.view_model.HistoryConfigViewModel
import com.hmis_tn.doctor.ui.emr_workflow.history.config.view_model.HistoryConfigViewModelFactory
import com.hmis_tn.doctor.ui.login.model.SimpleResponseModel
import com.hmis_tn.doctor.ui.login.model.login_response_model.UserDetails
import com.hmis_tn.doctor.utils.custom_views.CustomProgressDialog
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response

class ConfigCCCFragment : DialogFragment(), StartDragListener {

    private var facility_id: Int? = 0
    private var DepartmentID: Int? = 0
    private var utils: Utils? = null
    private var userDataStoreBean: UserDetails? = null
    private var configFinalData: ArrayList<CriticalCareChartConfigListData> = ArrayList()
    private var binding: FragmentHisoryConfigBinding? = null
    private var viewModel: HistoryConfigViewModel? = null
    private var configadapter: CccConfigMainRecyclerAdapter? = null
    private var configfavadapter: CccConfigListRecyclerAdapter? = null

    internal var touchHelper: ItemTouchHelper? = null
    private var customProgressDialog: CustomProgressDialog? = null
    var requestparameter: ConfigUpdateRequestModel? = ConfigUpdateRequestModel()
    private var configRequestData: ArrayList<CriticalCareChartConfigListData?>? = ArrayList()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var appPreferences: AppPreferences? = null
    var ConfigStatus: Boolean? = false

    companion object {
        var callbackHistoryRefresh: OnsaveHistoryRefreshListener? = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            if (dialog.window != null)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.window?.attributes?.windowAnimations = R.style.CardDialogAnimation
            isCancelable = false
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_hisory_config, container, false)
        viewModel = HistoryConfigViewModelFactory(
            requireActivity().application
        ).create(HistoryConfigViewModel::class.java)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this

        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        DepartmentID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        customProgressDialog = CustomProgressDialog(requireContext())
        utils = Utils(requireContext())

        ///Config
/*        viewModel!!.progress.observe(requireActivity(),
            Observer { progress ->
                if (progress == View.VISIBLE) {
                    customProgressDialog!!.show()
                } else if (progress == View.GONE) {
                    customProgressDialog!!.dismiss()
                }
            })
        viewModel!!.errorText.observe(requireActivity(),
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })*/

        setinitView()
        setAdapter()
        setAdapterMainCallback()
        setAdapterAddCallback()
        setButtonFunction()
        setinitAPI()


        return binding!!.root

    }

    private fun setinitAPI() {
        getCriticalCareChartFilterHeadings()
    }

    private fun setButtonFunction() {
        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        binding!!.clearCardView.setOnClickListener {
            val conficFinalDat = configfavadapter?.getFinalData()
            for (i in conficFinalDat!!.indices) {
                configadapter?.setConfigfavList(conficFinalDat.get(i))
            }
            configfavadapter?.clearALL()
            binding?.conficount?.text = configadapter?.getItemSize().toString()
            binding?.confifavcount?.text = configfavadapter?.getItemSize().toString()
        }

        binding!!.click.setOnClickListener {
            val conficFinalData = configfavadapter?.getFinalData()
            val reqestData: ArrayList<SaveCriticalCareChartConfig> = ArrayList()
            if (conficFinalData?.size!! > 0) {
                for (i in conficFinalData.indices) {
                    reqestData.add(
                        SaveCriticalCareChartConfig(
                            critical_care_type_uuid = conficFinalData[i].uuid,
                            department_uuid = DepartmentID.toString(),
                            facility_uuid = facility_id.toString()
                        )
                    )
                }

                viewModel?.updateConfig(
                    facility_id,
                    reqestData,
                    getUpdateConfigCallback
                )

                /*
                    viewModel?.postRequestParameter(
                        facility_id,
                        configRequestData!!,
                        configFinalRetrofitCallBack,
                        ConfigStatus
                    )
                    */

            } else {
                utils!!.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Please add any one item"
                )
            }
        }
        binding?.moveall!!.setOnClickListener {

            val conficFinalDat = configadapter?.getConfigData()

            if (conficFinalDat?.size == 0) {
                Toast.makeText(requireContext(), "All item moved already", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            configfavadapter?.clearALL()

            for (i in conficFinalDat!!.indices) {
                configfavadapter?.setConfigfavList(conficFinalDat.get(i))
            }
            configadapter?.removeall(conficFinalDat)


            binding?.conficount?.text = configadapter?.getItemSize().toString()

            binding?.confifavcount?.text = configfavadapter?.getItemSize().toString()

        }


    }

    private fun setinitView() {


        binding?.confiname?.text = "Critical Care Chart"

        binding?.conficount?.textSize
        //Search get data

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

    }

    private fun setAdapterAddCallback() {

        configfavadapter!!.setOnItemClickListener(object :
            CccConfigListRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(
                configfavResponseContent: CriticalCareChartConfigListData,
                position: Int
            ) {


                configadapter?.setConfigList(configfavResponseContent)
                configfavadapter?.removeitem(position)
                binding?.conficount?.text = configadapter?.getItemSize().toString()
                binding?.confifavcount?.text = configfavadapter?.getItemSize().toString()

            }

        })

    }

    private fun setAdapterMainCallback() {


        configadapter!!.setOnItemClickListener(object :
            CccConfigMainRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(
                configResponseContent: CriticalCareChartConfigListData,
                position: Int
            ) {

                val conficFinalDat = configfavadapter?.getFinalData()
                val check = conficFinalDat!!.any { it.uuid == configResponseContent.uuid }
                if (!check) {
                    configfavadapter?.setConfigfavList(configResponseContent)
                    binding?.confifavcount?.text = configfavadapter?.getItemSize().toString()
                    //    configadapter?.removeitem(configResponseContent?.uuid)
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


    }

    private fun setAdapter() {
        configadapter = CccConfigMainRecyclerAdapter(requireContext(), ArrayList())
        binding!!.ConfigRecyclerView.adapter = configadapter
        configfavadapter = CccConfigListRecyclerAdapter(this, ArrayList())
        binding!!.ConfigfavRecyclerView.adapter = configfavadapter
    }

    override fun requestDrag(viewHolder: RecyclerView.ViewHolder) {
        touchHelper?.startDrag(viewHolder)
    }

    fun setOnSaveRefreshListener(callback: OnsaveHistoryRefreshListener) {
        callbackHistoryRefresh = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnsaveHistoryRefreshListener {
        fun onRefreshList()
    }


    private fun getCriticalCareChartHeadings() {
        val body = GetCriticalCareChartHeadingsReq("critical_care_types")
        viewModel?.getCriticalCareChartHeadings(
            facility_id!!,
            body,
            getCriticalCareChartHeadingsRespCallback
        )
    }


    private val getCriticalCareChartHeadingsRespCallback =
        object : RetrofitCallback<GetCriticalCareChartHeadingsResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetCriticalCareChartHeadingsResp>?) {
                responseBody?.body()?.let { getCriticalCareChartHeadingsResp ->
                    getCriticalCareChartHeadingsResp.responseContents?.let { list ->
                        configadapter!!.clearALL()
                        for (i in list.indices) {
                            val check = configFinalData.any { it.uuid == list[i].uuid }
                            if (!check) {
                                configadapter!!.addData(
                                    CriticalCareChartConfigListData(
                                        uuid = list[i].uuid!!,
                                        name = list[i].name!!
                                    )
                                )
                            }
                        }
                        binding?.conficount?.text = configadapter?.getItemSize().toString()
                        binding?.confifavcount?.setText(configfavadapter?.getItemSize().toString())
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<GetCriticalCareChartHeadingsResp>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
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

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private fun getCriticalCareChartFilterHeadings() {
        viewModel?.getCriticalCareChartFilterHeadings(
            facility_id!!,
            getCriticalCareChartFilterHeadingsCallback
        )
    }


    private val getCriticalCareChartFilterHeadingsCallback =
        object : RetrofitCallback<CriticalCareChartFilterHeadingsResponse> {
            override fun onSuccessfulResponse(responseBody: Response<CriticalCareChartFilterHeadingsResponse>?) {
                responseBody?.body()?.let { getCriticalCareChartHeadingsResp ->
                    getCriticalCareChartHeadingsResp.responseContents?.let { list ->
                        configFinalData.clear()
                        for (i in list.indices) {
                            configFinalData.add(
                                CriticalCareChartConfigListData(
                                    uuid = list[i].cc_type_id!!,
                                    name = list[i].cc_type_name!!
                                )
                            )
                        }
                        configfavadapter?.setData(configFinalData)
                        getCriticalCareChartHeadings()
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<CriticalCareChartFilterHeadingsResponse>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
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

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }


    private val getUpdateConfigCallback =
        object : RetrofitCallback<SimpleResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<SimpleResponseModel>?) {


                callbackHistoryRefresh!!.onRefreshList()
                dialog!!.dismiss()

            }

            override fun onBadRequest(errorBody: Response<SimpleResponseModel>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
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

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }
}