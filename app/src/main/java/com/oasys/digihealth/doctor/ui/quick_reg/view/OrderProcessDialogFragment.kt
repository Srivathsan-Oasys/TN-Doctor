package com.oasys.digihealth.doctor.ui.quick_reg.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.DialogOrderProcessListBinding
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.RecyclerDto
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.request.DirectApprovelReq
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.request.SendIdList
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.request.orderRequest.Header
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.request.orderRequest.OrderToProcessReqestModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response.OrderProcessResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response.SimpleResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.template.GetNoteTemplateReq
import com.oasys.digihealth.doctor.ui.quick_reg.model.template.GetNoteTemplateResp
import com.oasys.digihealth.doctor.ui.quick_reg.view_model.LabTestViewModel
import com.oasys.digihealth.doctor.ui.quick_reg.view_model.LabTestViewModelFactory
import com.oasys.digihealth.doctor.utils.CustomProgressDialog
import com.oasys.digihealth.doctor.utils.OldRichTextEditorDialogFragment
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderProcessDialogFragment : DialogFragment() {

    private var OrderData: ArrayList<SendIdList>? = ArrayList()
    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var content: String? = null
    private var viewModel: LabTestViewModel? = null
    var callbackOrderProcess: OnOrderProcessListener? = null
    var binding: DialogOrderProcessListBinding? = null
    private val labTestList: MutableList<orderList> = ArrayList()
    private val searchList: MutableList<RecyclerDto> = ArrayList()
    private var mAdapter: OrderProcessAdapter? = null
    var callbackLabTestCallBack: OnLabTestCallBack? = null
    private var favouriteData: ArrayList<Header> = ArrayList()

    private var favouritesize: Int = 1
    private var customProgressDialog: CustomProgressDialog? = null


    var covid: String = "COVID"

    var From: String = ""

    var testMethodCode: String = ""
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null

    private var noteTemplatePosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
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
            DataBindingUtil.inflate(inflater, R.layout.dialog_order_process_list, container, false)
        viewModel = LabTestViewModelFactory(
            requireActivity().application

        ).create(LabTestViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils = Utils(requireContext())
        customProgressDialog = CustomProgressDialog(requireContext())

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        appPreferences = AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
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
            dialog?.dismiss()
        }


        binding?.cancelCardView?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.rejectcardview?.setOnClickListener {

            callbackLabTestCallBack?.onArrayList(OrderData)
            dialog?.dismiss()

        }


        mAdapter = OrderProcessAdapter(requireContext(), ArrayList())
        val args = arguments

        if (args == null) {

        } else {
            // get value from bundle..
            favouriteData = args.getParcelableArrayList<Header>(AppConstants.RESPONSECONTENT)!!

            OrderData = args.getParcelableArrayList<SendIdList>(AppConstants.RESPONSEORDERARRAY)

            favouritesize = args.getInt(AppConstants.RESPONSENEXT)!!

            testMethodCode = args.getString("testMethodCode")!!

            From = args.getString("From")!!

            Log.i("respose", "$favouritesize  " + favouriteData)

            if (favouriteData[0].test_master.value_type_master.code != AppConstants.ANALYTECODE) {
                binding!!.analytiesview!!.visibility = View.GONE

//                val datasize=(favouriteData.size-1)/favouritesize

                val list: ArrayList<orderList> = ArrayList()

                val ol: orderList = orderList()

                ol.title = favouriteData[0].test_master.name

                if (favouriteData[0].result_value != null && favouriteData[0].result_value != "") {
                    ol.name = favouriteData[0].result_value
                }

                if (favouriteData[0].test_ref_master != null) {
                    ol.value =
                        favouriteData[0].test_ref_master.min_value.toString() + "-" + favouriteData[0].test_ref_master.max_value.toString()
                }

                if (favouriteData[0].test_master.note_template_uuid != null) {
                    ol.note_template_uuid = favouriteData[0].test_master.note_template_uuid ?: 0
                }


                if (favouriteData[0].test_master.analyte_uom != null) {
                    ol.umo =
                        favouriteData[0].test_master.analyte_uom.name.toString()
                }

                ol.type = favouriteData[0].test_master.value_type_master.code

                ol.code = favouriteData[0].test_master.code

                if (favouriteData[0].test_master.list_of_value != "" && favouriteData[0].test_master.list_of_value != null) {

                    val dats = favouriteData[0].test_master.list_of_value

                    val replace1 = dats.replace("[", "")

                    val replace2 = replace1.replace("]", "")

                    var ref = Utils.stringToWords(replace2)

                    ol.spinnerdata = ref as ArrayList<String>

                    var spinnerdata = ref as ArrayList<String>

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

                list.add(ol)

                mAdapter!!.setAll(list)

            } else {

                val list: ArrayList<orderList> = ArrayList()

                binding!!.analytiesview!!.visibility = View.VISIBLE

                binding!!.textProcess!!.text = favouriteData[0].test_master.name

                val datasize = (favouriteData.size / favouritesize) - 1

                Log.i("", "rrrrrrrrrrrrr" + favouritesize)

                for (i in 0..datasize) {

                    val ol: orderList = orderList()

                    if (favouriteData[i].result_value != null && favouriteData[i].result_value != "") {
                        ol.name = favouriteData[i].result_value
                    }

//                    if (favouriteData[i].test_master.note_template_uuid != null) {
//                        ol.note_template_uuid = favouriteData[i].test_master.note_template_uuid ?: 0
//                    }


                    if (favouriteData[i].analyte_master != null) {

                        ol.title = favouriteData[i].analyte_master.name

                        ol.code = favouriteData[i].test_master.code

                        if (favouriteData[i].analyte_ref_master != null) {
                            ol.value =
                                favouriteData[i].analyte_ref_master.min_value.toString() + "-" + favouriteData[i].analyte_ref_master.max_value.toString()
                        }


                        if (favouriteData[i].analyte_master.analyte_uom != null) {
                            ol.umo =
                                favouriteData[i].analyte_master.analyte_uom.name.toString()
                        }

                        ol.type = favouriteData[i].analyte_master.value_type_master.code

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

                            var spinnerdata = ref as ArrayList<String>

                            var spinnerData: MutableMap<Int, String> = mutableMapOf()
                            var spinner: MutableMap<String, Int> = mutableMapOf()

                            if (favouriteData[i].test_master.code == "COVID") {

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

                            val check = formula.contains("(", ignoreCase = true)

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

                                    if (favouriteData[j].analyte_master != null) {

                                        if (ref[k] == favouriteData[j].analyte_master.code) {

                                            array.add(j)

                                            list.add(j)

                                            ol.formulapostion = list

                                        }

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

                                    if (favouriteData[j].analyte_master != null) {

                                        if (ref[k] == favouriteData[j].analyte_master.code) {

                                            ol.PostionToData = PostionToData

                                            ol.DataTOPostion = DataTOPostion

                                            ol.formula = formu

                                        }

                                    }

                                    array.add(i)
                                }


                            }

                            //ol.spinnerdata = ref as ArrayList<String>

                        }

                        Log.i("", "" + ol)

                        list.add(ol)

                    }

                }

                mAdapter!!.setAll(list)


            }

        }
        val layoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

        binding?.orderProcessListrecycleview!!.layoutManager = layoutmanager

        binding?.orderProcessListrecycleview!!.adapter = mAdapter

        binding?.orderProcessListrecycleview!!.setNestedScrollingEnabled(false);

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")

        binding!!.saveOrder.setOnClickListener {

            val data = mAdapter!!.getAll()

            if (data.size != 0) {

                var resultSize = data.size

                var savestatus: Boolean = true

                for (i in favouriteData.indices) {

                    if (i < resultSize) {

                        favouriteData[i].result_value = data[i].name.toString()
                        if (data[i].name == "" || data[i].name == null) {

                            savestatus = false
                        }

                        if (testMethodCode == covid) {
                            favouriteData[i].qualifier_uuid = data[i].id!!
                            favouriteData[i].qualifierid = data[i].id!!
                        }
                        favouriteData[i].tat_session_end = sdf.format(Date())
                        favouriteData[i].tat_session_start = sdf.format(Date())

                    } else {

                        favouriteData[i].result_value = data[i % resultSize].name.toString()

                        if (data[i % resultSize].name == "" || data[i % resultSize].name == null) {

                            savestatus = false
                        }


                        if (testMethodCode == covid) {
                            favouriteData[i].qualifier_uuid = data[i % resultSize].id!!
                            favouriteData[i].qualifierid = data[i % resultSize].id!!
                        }
                        favouriteData[i].tat_session_end = sdf.format(Date())
                        favouriteData[i].tat_session_start = sdf.format(Date())

                    }
                }

                if (savestatus) {
                    val request: OrderToProcessReqestModel = OrderToProcessReqestModel()

                    request.header = favouriteData

                    var jsonresponse = Gson().toJson(favouriteData)

                    Log.i("json", "" + jsonresponse)

                    viewModel!!.orderProcess(request, orderProcessRetrofitCallback)
                } else {

                    Toast.makeText(context, "Please Select all Result", Toast.LENGTH_SHORT).show()

                }

            } else {

                Toast.makeText(this.context, "Cannot be process", Toast.LENGTH_SHORT).show()

            }
        }


        binding!!.approvedOrder!!.setOnClickListener {

            val data = mAdapter!!.getAll()

            var savestatus: Boolean = true


            val patentid = favouriteData[0].patient_order_test_detail_uuid
            if (data.size != 0) {

                var resultSize = data.size

                for (i in favouriteData.indices) {

                    if (i < resultSize) {

                        favouriteData[i].result_value = data[i].name.toString()

                        if (data[i].name == "" || data[i].name == null) {

                            savestatus = false
                        }


                        if (testMethodCode == covid) {
                            favouriteData[i].qualifier_uuid = data[i].id!!
                            favouriteData[i].qualifierid = data[i].id!!
                        }
                        favouriteData[i].patient_order_test_detail_uuids = patentid
                        favouriteData[i].tat_session_end = sdf.format(Date())
                        favouriteData[i].tat_session_start = sdf.format(Date())

                    } else {

                        favouriteData[i].result_value = data[i % resultSize].name.toString()

                        if (data[i % resultSize].name == "" || data[i % resultSize].name == null) {

                            savestatus = false
                        }

                        if (testMethodCode == covid) {
                            favouriteData[i].qualifier_uuid = data[i % resultSize].id!!
                            favouriteData[i].qualifierid = data[i % resultSize].id!!
                        }

                        favouriteData[i].patient_order_test_detail_uuids = patentid
                        favouriteData[i].tat_session_end = sdf.format(Date())
                        favouriteData[i].tat_session_start = sdf.format(Date())

                    }
                }

                if (savestatus) {

                    val request: DirectApprovelReq = DirectApprovelReq()

                    request.details = favouriteData

                    var jsonresponse = Gson().toJson(favouriteData)

                    Log.i("json", "" + jsonresponse)

                    viewModel!!.orderDirectApprovel(request, orderProcessDirectRetrofitCallback)

                } else {
                    Toast.makeText(context, "Please Select all Result", Toast.LENGTH_SHORT).show()

                }

            } else {

                Toast.makeText(this.context, "Cannot be process", Toast.LENGTH_SHORT).show()

            }
        }


        mAdapter?.setOnCommandClickListener(object : OrderProcessAdapter.OnCommandClickListener {
            override fun onCommandClick(position: Int, Command: String, noteTemplateUuid: Int) {

                if (Command.trim() == "") {
                    noteTemplatePosition = position
                    getNoteTemplate(noteTemplateUuid)
                } else {

                    val ft = childFragmentManager.beginTransaction()

                    val richTextEditorDialog = OldRichTextEditorDialogFragment(
                        mContext = context!!,
                        title = "Note Template",
                        body = Command ?: "",
                        stringAsHtml = { stringAsHtml ->
                            //returns HTML when save is clicked
                            mAdapter?.setTemplateData(position, stringAsHtml)
                            println(stringAsHtml)
                        }
                    )
                    richTextEditorDialog.show(ft, "Tag")
                }


            }
        })


        return binding?.root
    }

    private fun getNoteTemplate(noteTempId: Int) {
        val body = GetNoteTemplateReq(noteTempId)
        viewModel?.getNoteTemplate(facilitylevelID!!, body, getNoteTemplateRespCallback)
    }

    val orderProcessRetrofitCallback = object :
        RetrofitCallback<OrderProcessResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<OrderProcessResponseModel>?) {

            callbackOrderProcess?.onRefreshOrderList("")
            dialog!!.dismiss()

        }

        override fun onBadRequest(errorBody: Response<OrderProcessResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: OrderProcessResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    OrderProcessResponseModel::class.java
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

    val orderProcessDirectRetrofitCallback = object :
        RetrofitCallback<SimpleResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<SimpleResponseModel>?) {

            /*        if(From=="Test") {

                        AnalyticsManager.getAnalyticsManager().trackLMISLabTestOrderApproval(context!!,"")

                    }
                    else{

                        AnalyticsManager.getAnalyticsManager().trackLMISLabProcessOrderApproval(context!!, "")
                    }

        */
            callbackOrderProcess?.onRefreshOrderList(From)
            dialog!!.dismiss()

        }

        override fun onBadRequest(errorBody: Response<SimpleResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: OrderProcessResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    OrderProcessResponseModel::class.java
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

    private val getNoteTemplateRespCallback =
        object : RetrofitCallback<GetNoteTemplateResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetNoteTemplateResp>?) {
                responseBody?.body()?.let { getNoteTemplateResp ->

                    getNoteTemplateResp.responseContents?.let { responseContents ->
                        val ft = childFragmentManager.beginTransaction()

                        //this lib displays table but ignoring the whole text if \n is present
//                        val richTextEditorDialog = RichTextEditorViewDialogFragment(
//                            mContext = context!!,
//                            title = "Note Template",
//                            body = responseContents.data_template ?: "",
//                            stringAsHtml = { stringAsHtml ->
//
//                                mAdapter?.setTemplateData(noteTemplatePosition, stringAsHtml)
//
//                                println(stringAsHtml)
//                            }
//                        )
//                        richTextEditorDialog.show(ft, "Tag")

                        val richTextEditorDialog = OldRichTextEditorDialogFragment(
                            mContext = context!!,
                            title = "Note Template",
                            body = responseContents.data_template ?: "",
                            stringAsHtml = { stringAsHtml ->

                                mAdapter?.setTemplateData(noteTemplatePosition, stringAsHtml)

                                println(stringAsHtml)
                            }
                        )
                        richTextEditorDialog.show(ft, "Tag")

                    } ?: handleEmpty()
                }
            }

            override fun onBadRequest(errorBody: Response<GetNoteTemplateResp>?) {
                val gson = GsonBuilder().create()
                val responseModel: OrderProcessResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody!!.errorBody()!!.string(),
                        OrderProcessResponseModel::class.java
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

    private fun handleEmpty() {
        val ft = childFragmentManager.beginTransaction()
        val richTextEditorDialog = OldRichTextEditorDialogFragment(
            mContext = requireContext(),
            title = "Note Template",
            body = "",
            stringAsHtml = { stringAsHtml ->

                mAdapter?.setTemplateData(noteTemplatePosition, stringAsHtml)

                println(stringAsHtml)
            }
        )
        richTextEditorDialog.show(ft, "Tag")
    }

    fun setOnOrderProcessRefreshListener(callback: OnOrderProcessListener) {
        callbackOrderProcess = callback
    }

    // or a separate test implementation.
    interface OnOrderProcessListener {
        fun onRefreshOrderList(from: String)
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