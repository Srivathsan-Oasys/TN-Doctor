package com.hmis_tn.doctor.ui.quick_reg.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.extention.alert
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogApprovalResultListBinding
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.radiology.model.RecyclerDto
import com.hmis_tn.doctor.ui.quick_reg.model.labapprovalresult.ApprovalRequestModel
import com.hmis_tn.doctor.ui.quick_reg.model.labapprovalresult.LabApprovalSpinnerResponseContent
import com.hmis_tn.doctor.ui.quick_reg.model.labapprovalresult.LabApprovalSpinnerResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.labapprovalresult.Row
import com.hmis_tn.doctor.ui.quick_reg.model.labapprovalresult.response.OrderApprovedResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.SendIdList
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.orderRequest.Req
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.SimpleResponseModel
import com.hmis_tn.doctor.ui.quick_reg.view_model.LabTestApprovalViewModel
import com.hmis_tn.doctor.ui.quick_reg.view_model.LabTestApprovalViewModelFactory
import com.hmis_tn.doctor.utils.custom_views.CustomProgressDialog
import com.hmis_tn.doctor.utils.OldRichTextEditorDialogFragment
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TestApprovalResultDialogFragment : DialogFragment() {

    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var approvalResult = mutableMapOf<Int, String>()

    private var content: String? = null
    private var viewModel: LabTestApprovalViewModel? = null
    var binding: DialogApprovalResultListBinding? = null
    private val labTestList: MutableList<RecyclerDto> = ArrayList()
    private var mAdapter: ApprovalResultAdapter? = null
    private var customProgressDialog: CustomProgressDialog? = null
    private var favouriteData: ArrayList<Row> = ArrayList()

    var callbackLabTestCallBack: OnLabTestCallBack? = null
    private var idlist: ArrayList<Int> = ArrayList()

    private var OrderData: ArrayList<SendIdList>? = ArrayList()

    private var selectAuthId: Int = 0
    var callbackTestApprovalResult: OnLabTestApprovalActivityRefreshListener? = null

    var covid: String = "COVID"

    private var testMethodCode: String = ""


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

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilitylevelID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        viewModel!!.progress.observe(requireActivity(), Observer { progress ->
            if (progress == View.VISIBLE) {
                customProgressDialog!!.show()
            } else if (progress == View.GONE) {
                customProgressDialog!!.dismiss()
            }

        })
        binding?.closeImageView?.setOnClickListener {
            //   dialog?.dismiss()

            dialog!!.dismiss()
            callbackTestApprovalResult!!.onRefreshLabTestApprovalList()
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

            OrderData = args.getParcelableArrayList<SendIdList>(AppConstants.RESPONSEORDERARRAY)


            testMethodCode = args.getString("testMethodCode")!!

            Log.i("respose", "" + favouriteData)

            val list: ArrayList<orderList> = ArrayList()

            idlist.clear()

            /*for(i in favouriteData!!.indices){

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
            mAdapter!!.setAll(list)*/


            if (favouriteData[0].test_master.value_type_master.code != AppConstants.ANALYTECODE) {

                binding!!.analytiesview!!.visibility = View.GONE

                val list: ArrayList<orderList> = ArrayList()

                val ol: orderList = orderList()

                idlist.add(favouriteData[0].uuid)

                ol.title = favouriteData[0].test_master.name

                if (favouriteData[0].test_ref_master != null) {
                    ol.value =
                        favouriteData[0].test_ref_master.min_value.toString() + "-" + favouriteData[0].test_ref_master.max_value.toString()
                }


                if (favouriteData[0].test_master.analyte_uom != null) {
                    ol.umo =
                        favouriteData[0].test_master.analyte_uom.name.toString()
                }

                ol.type = favouriteData[0].test_master.value_type_master.code

                val dats = favouriteData[0].test_master.list_of_value

                if (dats != "" && dats != null) {

                    val replace1 = dats.replace("[", "")

                    val replace2 = replace1.replace("]", "")

                    var ref = Utils.stringToWords(replace2)

                    ol.spinnerdata = ref as ArrayList<String>


                    var spinnerdata = ref

                    var spinnerData: MutableMap<Int, String> = mutableMapOf()
                    var spinner: MutableMap<String, Int> = mutableMapOf()

                    if (favouriteData[0].test_master.code == "COVID") {

                        for (i in spinnerdata.indices) {

                            val data = spinnerdata[i]

                            when (data) {
                                "Positive" -> {

                                    spinnerData[2] = data
                                }
                                "Negative" -> {

                                    spinnerData[1] = data
                                }
                                "Equivocal" -> {

                                    spinnerData[3] = data
                                }
                            }

                            spinner[data] = i


                        }

                        ol.spinner = spinner

                        ol.spinnerData = spinnerData

                    } else {
                        for (i in spinnerdata.indices) {

                            val data = spinnerdata[i]

                            spinnerData[i] = data

                            spinner[data] = i

                        }

                        ol.spinner = spinner

                        ol.spinnerData = spinnerData

                    }

                }
                ol.name = favouriteData[0].result_value

                list.add(ol)

                mAdapter!!.setAll(list)

            } else {

                val list: ArrayList<orderList> = ArrayList()

                binding!!.analytiesview!!.visibility = View.VISIBLE

                binding!!.textProcess!!.text = favouriteData[0].test_master.name

                for (i in favouriteData.indices) {

                    if (favouriteData[i].analyte_master != null) {


                        idlist.add(favouriteData[i].uuid)

                        val ol: orderList = orderList()

                        ol.title = favouriteData[i].analyte_master.name

                        if (favouriteData[i].test_ref_master != null) {
                            ol.value =
                                favouriteData[i].test_ref_master.min_value.toString() + "-" + favouriteData[i].test_ref_master.max_value.toString()
                        }


                        if (favouriteData[i].analyte_master.analyte_uom != null) {
                            ol.umo =
                                favouriteData[i].analyte_master.analyte_uom.name.toString()
                        }

                        ol.type = favouriteData[i].analyte_master.value_type_master.code

                        ol.name = favouriteData[i].result_value

                        var dats = favouriteData[i].analyte_master.list_of_value

                        if (dats != null && dats != "") {

                            var check = dats.contains("[", ignoreCase = true)

                            if (check) {

                                dats = dats.replace("[", "")

                            }
                            val check2 = dats.contains("]", ignoreCase = true)

                            if (check2) {

                                dats = dats.replace("]", "")

                            }


                            var ref = Utils.stringToWords(dats)

                            ol.spinnerdata = ref as ArrayList<String>

                            var spinnerdata = ref

                            var spinnerData: MutableMap<Int, String> = mutableMapOf()
                            var spinner: MutableMap<String, Int> = mutableMapOf()


                            if (favouriteData[0].test_master.code == "COVID") {

                                for (j in spinnerdata.indices) {

                                    val data = spinnerdata[j]

                                    when (data) {
                                        "Positive" -> {

                                            spinnerData[2] = data
                                        }
                                        "Negative" -> {

                                            spinnerData[1] = data
                                        }
                                        "Equivocal" -> {

                                            spinnerData[3] = data
                                        }
                                    }

                                    spinner[data] = j


                                }

                                ol.spinner = spinner

                                ol.spinnerData = spinnerData

                            } else {
                                for (j in spinnerdata.indices) {

                                    val data = spinnerdata[j]

                                    spinnerData[j] = data

                                    spinner[data] = j

                                }

                                ol.spinner = spinner

                                ol.spinnerData = spinnerData

                            }


                        }

                        var formula = favouriteData[i].analyte_master.formula

                        if (formula != null && formula != "") {

                            var check = formula.contains("(", ignoreCase = true)

                            if (check) {

                                formula = formula.replace("(", "")

                            }
                            val check2 = formula.contains(")", ignoreCase = true)

                            if (check2) {

                                formula = formula.replace(")", "")

                            }

                            val plus = formula.contains("+", ignoreCase = true)
                            val minus = formula.contains("-", ignoreCase = true)
                            val sup = formula.contains("*", ignoreCase = true)
                            val divided = formula.contains("/", ignoreCase = true)

                            var array: ArrayList<Int> = ArrayList()

                            var PostionToData = mutableMapOf<Int, Int>()

                            var DataTOPostion = mutableMapOf<Int, Int>()

                            var formu: String = ""

                            when {
                                plus -> {
                                    formu = "+"
                                }
                                minus -> {
                                    formu = "-"
                                }
                                sup -> {

                                    formu = "*"
                                }
                                divided -> {

                                    formu = "/"
                                }
                            }

                            var ref = Utils.stringToWords(formula, formu)

                            Log.i("", "" + ref)

                            var list: ArrayList<Int> = ArrayList()

                            list.clear()

                            for (k in ref.indices) {

                                for (j in favouriteData.indices) {

                                    if (ref[k] == favouriteData[j].analyte_master.code) {

                                        array.add(j)

                                        list.add(j)

                                        ol.formulapostion = list

                                    }


                                }

                            }

                            array.add(i)

                            for (k in array.indices) {

                                PostionToData[k] = array[k]

                                DataTOPostion[array[k]] = k


                            }

                            for (k in ref.indices) {

                                for (j in favouriteData.indices) {

                                    if (ref[k] == favouriteData[j].analyte_master.code) {

                                        ol.PostionToData = PostionToData

                                        ol.DataTOPostion = DataTOPostion

                                        ol.formula = formu

                                    }

                                    array.add(i)
                                }

                            }

                            //ol.spinnerdata = ref as ArrayList<String>

                        }

                        list.add(ol)

                    }

                }


                mAdapter!!.setAll(list)


            }

        }

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")

        binding!!.approved.setOnClickListener {

            val data = mAdapter!!.getAll()

            var savestatus: Boolean = true

            if (testMethodCode == "COVID") {

                for (i in favouriteData.indices) {

                    favouriteData[i].result_value = data[0].name.toString()

                    if (data[0].name == "" || data[0].name == null) {

                        savestatus = false
                    }
                    if (testMethodCode == covid) {
                        favouriteData[i].qualifier_uuid = data[0].id!!
                        favouriteData[i].qualifierid = data[0].id!!
                    }
                    favouriteData[i].tat_session_end = sdf.format(Date())
                    favouriteData[i].tat_session_start = sdf.format(Date())

                }
            } else {


                for (i in favouriteData.indices) {

                    favouriteData[i].result_value = data[i].name.toString()

                    if (data[i].name == "" || data[i].name == null) {

                        savestatus = false
                    }
                    if (testMethodCode == covid) {
                        favouriteData[i].qualifier_uuid = data[0].id!!
                        favouriteData[i].qualifierid = data[0].id!!
                    }
                    favouriteData[i].tat_session_end = sdf.format(Date())
                    favouriteData[i].tat_session_start = sdf.format(Date())

                }
            }

            if (savestatus) {

                val request: ApprovalRequestModel = ApprovalRequestModel()

                request.Id = idlist

                request.details = favouriteData

                request.auth_status_uuid = selectAuthId

                viewModel!!.orderApproved(request, orderProcessRetrofitCallback)

            } else {

                Toast.makeText(context, "Please Select all Result", Toast.LENGTH_SHORT).show()
            }
        }


        binding?.retest?.setOnClickListener {

            //   Log.i("",""+favouriteData[0].patient_order_test_detail_uuid)

            var data: ArrayList<Int> = ArrayList()



            for (i in favouriteData.indices) {

                data.add(favouriteData[i].uuid)

            }

            requireContext().alert(
                title = "Retest",
                msg = "Are you sure want to continue?",
                yes = {
                    viewModel?.retest(data, orderretestRetrofitCallback)
                },
                no = {

                })
        }

        binding?.reject?.setOnClickListener {
            callbackLabTestCallBack?.onArrayList(OrderData)
            dialog?.dismiss()
        }

        mAdapter?.setOnCommandClickListener(object : ApprovalResultAdapter.OnCommandClickListener {
            override fun onCommandClick(position: Int, Command: String) {
                val ft = childFragmentManager.beginTransaction()
                val richTextEditorDialog = OldRichTextEditorDialogFragment(
                    mContext = context!!,
                    title = "Note Template",
                    body = Command,
                    stringAsHtml = { stringAsHtml ->
                        //returns HTML when save is clicked
                        mAdapter?.setTemplateData(position, stringAsHtml)
                        println(stringAsHtml)
                    }
                )
                richTextEditorDialog.show(ft, "Tag")
            }
        })
        return binding?.root
    }

    fun setApprovalSpinnerValue(responseContents: List<LabApprovalSpinnerResponseContent?>?) {

        val addlistData: ArrayList<LabApprovalSpinnerResponseContent?> = ArrayList()
        val dummy: LabApprovalSpinnerResponseContent = LabApprovalSpinnerResponseContent()
        dummy.name = "Please select"
        addlistData.add(dummy)
        addlistData.addAll(responseContents!!)
        approvalResult = addlistData.map { it?.uuid!! to it.name }.toMap().toMutableMap()

        val adapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.spinner_item,
            approvalResult.values.toMutableList()
        )

        adapter.setDropDownViewResource(R.layout.spinner_item)

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

    val orderProcessRetrofitCallback = object :
        RetrofitCallback<OrderApprovedResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<OrderApprovedResponseModel>?) {
            dialog!!.dismiss()
            callbackTestApprovalResult!!.onRefreshLabTestApprovalList()
            //  labListAPI()
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
                    getString(R.string.something_went_wrong)
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

    val orderretestRetrofitCallback = object :
        RetrofitCallback<SimpleResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<SimpleResponseModel>?) {


            dialog!!.dismiss()
            callbackTestApprovalResult!!.onRefreshLabTestApprovalList()
            //  labListAPI()


        }

        override fun onBadRequest(errorBody: Response<SimpleResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: SimpleResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    SimpleResponseModel::class.java
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

    fun setOnLabTestApprovalRefreshListener(callback: OnLabTestApprovalActivityRefreshListener) {
        callbackTestApprovalResult = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnLabTestApprovalActivityRefreshListener {
        fun onRefreshLabTestApprovalList()
    }


    fun setOnLabTestProcess(callback: OnLabTestCallBack) {
        callbackLabTestCallBack = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnLabTestCallBack {

        fun onArrayList(orderData: ArrayList<SendIdList>?)
    }
}
