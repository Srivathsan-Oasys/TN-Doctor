package com.oasys.digihealth.doctor.ui.emr_workflow.history.config.view

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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentHisoryConfigBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.configuration.model.*
import com.oasys.digihealth.doctor.ui.configuration.view.ConfigFavRecyclerAdapter
import com.oasys.digihealth.doctor.ui.configuration.view.ConfigRecyclerAdapter
import com.oasys.digihealth.doctor.ui.configuration.view.ItemMoveCallback
import com.oasys.digihealth.doctor.ui.configuration.view.StartDragListener
import com.oasys.digihealth.doctor.ui.emr_workflow.history.config.view_model.HistoryConfigViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.config.view_model.HistoryConfigViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.history.model.response.HistoryResponce
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EmrWorkFlowResponseModel
import com.oasys.digihealth.doctor.utils.custom_views.CustomProgressDialog
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response


class ConfigHistoryFragment : DialogFragment(), StartDragListener {

    private var facility_id: Int? = 0
    private var utils: Utils? = null
    private var conficFinalData: ArrayList<ConfigResponseContent?>? = ArrayList()
    private var binding: FragmentHisoryConfigBinding? = null
    private var viewModel: HistoryConfigViewModel? = null
    private var configadapter: ConfigRecyclerAdapter? = null
    private var configfavadapter: ConfigFavRecyclerAdapter? = null

    internal var touchHelper: ItemTouchHelper? = null
    private var customProgressDialog: CustomProgressDialog? = null
    var requestparameter: ConfigUpdateRequestModel? = ConfigUpdateRequestModel()
    private var configRequestData: ArrayList<HistoryConfigUpdateRequestModel?>? = ArrayList()
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
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val DepartmentID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        customProgressDialog = CustomProgressDialog(requireContext())
        utils = Utils(requireContext())
        viewModel?.getConfigList(configRetrofitCallBack)
        ///Config
        viewModel!!.progress.observe(requireActivity(),
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
            })

        configadapter = ConfigRecyclerAdapter(requireContext(), ArrayList())
        binding!!.ConfigRecyclerView.adapter = configadapter

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
        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        binding!!.clearCardView.setOnClickListener {
            conficFinalData = configfavadapter?.getFinalData()
            for (i in conficFinalData!!.indices) {
                configadapter?.setConfigfavList(conficFinalData!!.get(i))

            }
            configfavadapter?.clearALL()
            binding?.conficount?.text = configadapter?.getItemSize().toString()
            binding?.confifavcount?.text = configfavadapter?.getItemSize().toString()

        }




        configfavadapter = ConfigFavRecyclerAdapter(this, ArrayList())


        val callback = ItemMoveCallback(configfavadapter)
        touchHelper = ItemTouchHelper(callback)
        touchHelper!!.attachToRecyclerView(binding?.ConfigfavRecyclerView)


        binding!!.ConfigfavRecyclerView.adapter = configfavadapter


        binding!!.click.setOnClickListener {

            conficFinalData = configfavadapter?.getFinalData()

            if (conficFinalData?.size!! > 0) {
                for (i in conficFinalData?.indices!!) {
                    val configData: HistoryConfigUpdateRequestModel =
                        HistoryConfigUpdateRequestModel()
                    configData.facility_uuid = facility_id
                    configData.department_uuid = DepartmentID
                    configData.user_uuid = userDataStoreBean?.uuid
                    configData.role_uuid = 1
                    configData.context_uuid = conficFinalData?.get(i)?.context_uuid
                    configData.context_activity_map_uuid = conficFinalData?.get(i)!!.uuid
                    configData.activity_uuid = conficFinalData?.get(i)?.activity_uuid
                    configData.history_view_order = i + 1

                    configRequestData!!.add(configData)
                }
                viewModel?.postRequestParameter(
                    facility_id,
                    configRequestData!!,
                    configFinalRetrofitCallBack,
                    ConfigStatus
                )
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
            configfavadapter?.clearALL()

            for (i in conficFinalData!!.indices) {
                configfavadapter?.setConfigfavList(conficFinalData!!.get(i))
            }
            configadapter?.removeall(conficFinalData)


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

    override fun requestDrag(viewHolder: RecyclerView.ViewHolder) {
        touchHelper?.startDrag(viewHolder)
    }

    val configRetrofitCallBack = object : RetrofitCallback<ConfigResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<ConfigResponseModel>?) {

            configadapter?.setConfigItem(responseBody!!.body()?.responseContents!!)

            binding?.conficount?.text = responseBody!!.body()?.responseContents!!.size.toString()
            viewModel?.getHistoryList(facility_id!!, getHistoryAllResponseCallback)
//            viewModel?.getEmrWorkFlowFav(emrWorkFlowRetrofitCallBack)

        }

        override fun onBadRequest(errorBody: Response<ConfigResponseModel>?) {
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
        override fun onSuccessfulResponse(responseBody: Response<ConfigUpdateResponseModel>?) {
            utils!!.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                responseBody?.body()?.message!!
            )

            callbackHistoryRefresh!!.onRefreshhistoryList()
            dismiss()

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


    /* private val emrWorkFlowRetrofitCallBack = object : RetrofitCallback<EmrWorkFlowResponseModel> {
         override fun onSuccessfulResponse(response: Response<EmrWorkFlowResponseModel>) {
             if (response.body()?.responseContents?.isNotEmpty()!!) {

                 configfavadapter?.setConfigfavarrayList(response.body()?.responseContents!!)
                 binding?.confifavcount?.setText(configfavadapter?.getItemSize().toString())

             }
         }

         override fun onBadRequest(response: Response<EmrWorkFlowResponseModel>) {
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

         override fun onServerError(response: Response<*>) {
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

         override fun onFailure(failure: String) {
             utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
         }

         override fun onEverytime() {
             viewModel!!.progress.value = 8
         }
     }
 */
    val getHistoryAllResponseCallback = object : RetrofitCallback<HistoryResponce> {
        override fun onSuccessfulResponse(response: Response<HistoryResponce>?) {
            //Log.e("DataHistory", responseBody.toString())

            if (response!!.body()!!.responseContents!!.isNotEmpty()) {
                configfavadapter?.setConfigHistoryarrayList(response.body()!!.responseContents)
                binding?.confifavcount?.text = configfavadapter?.getItemSize().toString()
                ConfigStatus = false
            } else {

                ConfigStatus = true

            }

        }

        override fun onBadRequest(errorBody: Response<HistoryResponce>?) {
            //Log.e("DataHistory", "badRequest")
            val gson = GsonBuilder().create()
            val responseModel: EmrWorkFlowResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
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
            // Log.e("DataHistory", "servererr")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            //Log.e("DataHistory", "unAuth")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            //Log.e("DataHistory", "forbidden")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String?) {
            //Log.e("DataHistory", "failure")
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
        }

        override fun onEverytime() {
            //Log.e("DataHistory", "everytime")
            viewModel!!.progress.value = 8
        }

    }

    fun setOnSaveHistoryRefreshListener(callback: OnsaveHistoryRefreshListener) {
        callbackHistoryRefresh = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnsaveHistoryRefreshListener {
        fun onRefreshhistoryList()
    }


}