package com.oasys.digihealth.doctor.ui.quick_reg.view


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants

import com.oasys.digihealth.doctor.utils.Utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.DialogApprovalResultListBinding
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.RecyclerDto
import com.oasys.digihealth.doctor.ui.quick_reg.model.labapprovalresult.ApprovalRequestModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.labapprovalresult.LabApprovalSpinnerResponseContent
import com.oasys.digihealth.doctor.ui.quick_reg.model.labapprovalresult.LabApprovalSpinnerResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.labapprovalresult.Row
import com.oasys.digihealth.doctor.ui.quick_reg.model.labapprovalresult.response.OrderApprovedResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.request.orderRequest.Req
import com.oasys.digihealth.doctor.ui.quick_reg.view_model.*
import com.oasys.digihealth.doctor.utils.custom_views.CustomProgressDialog
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ConsolidatedReportResultDialogFragment : DialogFragment() {

    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var approvalResult = mutableMapOf<Int, String>()

    private var content: String? = null
    private var viewModel: LabTestApprovalViewModel? = null
    var binding: DialogApprovalResultListBinding? = null
    private val labTestList: MutableList<RecyclerDto> = ArrayList()
    private var mAdapter: ApprovalResultAdapter? = null
    private var customProgressDialog: CustomProgressDialog? = null
    private  var favouriteData:ArrayList<Row> =ArrayList()

    private  var idlist:ArrayList<Int> =ArrayList()

    private var selectAuthId:Int=0
    var callbackTestApprovalResult: OnLabConsolidatedReportActivityRefreshListener? = null


    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_approval_result_list,
                container,
                false
            )
        viewModel = LabTestApprovalViewModelFactory(
            requireActivity().application

        )
            .create(LabTestApprovalViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils = Utils(requireContext())
        customProgressDialog = CustomProgressDialog(requireContext())

        appPreferences = AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilitylevelID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        viewModel!!.progress.observe(requireActivity(), Observer {
                progress ->
            if (progress == View.VISIBLE) {
                customProgressDialog!!.show()
            } else if (progress == View.GONE) {
                customProgressDialog!!.dismiss()
            }

        })
        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.cancelCardView?.setOnClickListener {
            dialog?.dismiss()
        }

        val layoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding?.resultRecyclerView!!.layoutManager = layoutmanager
        mAdapter = ApprovalResultAdapter(requireContext(), ArrayList())
        binding?.resultRecyclerView!!.adapter = mAdapter
        val request: Req = Req()

        viewModel!!.getapprovalSpinner(ApprovalSpinnerRetrofitCallback)

        val args = arguments

        if (args == null) {

        } else {

            // get value from bundle..

            favouriteData = args.getParcelableArrayList<Row>(AppConstants.RESPONSECONTENT)!!

            Log.i("respose",""+favouriteData)

            val list:ArrayList<orderList> = ArrayList()

            idlist.clear()

            for(i in favouriteData!!.indices){

                val ol:orderList= orderList()

                idlist.add(favouriteData[i].uuid)

                ol.title=favouriteData[i].test_master.name

                if(favouriteData[i].qualifier_uuid!=null) {

                    ol.id = favouriteData[i].qualifier_uuid

                }

                ol.name=favouriteData[i].result_value

                if(favouriteData[0].test_master.list_of_value!="")
                {
                    ol.Status=1
                }

                else{
                    ol.Status=2
                }

                if (favouriteData[0].test_ref_master!=null) {
                    ol.value =
                        favouriteData[0].test_ref_master.min_value.toString() + "-" + favouriteData[0].test_ref_master.max_value.toString()
                }


                if (favouriteData[0].test_master.analyte_uom!=null) {
                    ol.umo =
                        favouriteData[0].test_master.analyte_uom.code.toString()
                }


                list.add(ol)
            }
            mAdapter!!.setAll(list)

        }

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")

        binding!!.approved.setOnClickListener {

            val data=mAdapter!!.getAll()

            for(i in favouriteData.indices){

                favouriteData[i].result_value= data[i].name.toString()
                if(data[0].id!=2) {
                    favouriteData[i].qualifier_uuid = data[0].id!!
                    favouriteData[i].qualifierid = data[0].id!!
                }
                favouriteData[i].tat_session_end= sdf.format(Date())
                favouriteData[i].tat_session_start= sdf.format(Date())

            }

            val request:ApprovalRequestModel= ApprovalRequestModel()

            request.Id=idlist

            request.details=favouriteData

            request.auth_status_uuid=selectAuthId

            viewModel!!.orderApproved(request,orderProcessRetrofitCallback)

        }

        return binding?.root
    }
    fun setApprovalSpinnerValue(responseContents: List<LabApprovalSpinnerResponseContent?>?) {

        approvalResult = responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        val adapter = ArrayAdapter<String>(activity!!, android.R.layout.simple_spinner_item, approvalResult.values.toMutableList())

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding?.assignedToSpinner!!.adapter = adapter


        binding?.assignedToSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectAuthId =
                        approvalResult.filterValues { it == itemValue }.keys.toList()[0]

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    selectAuthId =
                        approvalResult.filterValues { it == itemValue }.keys.toList()[0]



                }

            }


    }

    val ApprovalSpinnerRetrofitCallback = object :
        RetrofitCallback<LabApprovalSpinnerResponseModel> {
        @SuppressLint("LongLogTag")
        override fun onSuccessfulResponse(responseBody: Response<LabApprovalSpinnerResponseModel>?) {
            Log.i("ApprovalSpinnerRetrofitCallback", responseBody.toString())
            setApprovalSpinnerValue(responseBody?.body()?.responseContents)



            //  labListAPI()


        }

        override fun onBadRequest(errorBody: Response<LabApprovalSpinnerResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: LabApprovalSpinnerResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    LabApprovalSpinnerResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "something wrong"
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

    val orderProcessRetrofitCallback = object  :
        RetrofitCallback<OrderApprovedResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<OrderApprovedResponseModel>?) {

         callbackTestApprovalResult!!.onRefreshLabTestApprovalList()
            //  labListAPI()
            dialog!!.dismiss()

        }

        override fun onBadRequest(errorBody: Response<OrderApprovedResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: OrderApprovedResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    OrderApprovedResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "something wrong"
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

    fun setOnLabConsolidatedReportRefreshListener(callback: OnLabConsolidatedReportActivityRefreshListener) {
        callbackTestApprovalResult = callback
    }
    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnLabConsolidatedReportActivityRefreshListener {
        fun onRefreshLabTestApprovalList()
    }


}

