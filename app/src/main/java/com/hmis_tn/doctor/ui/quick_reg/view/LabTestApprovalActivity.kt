package com.hmis_tn.doctor.ui.quick_reg.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.ActivityLabApprovalTestBinding
import com.hmis_tn.doctor.fire_base_analytics.AnalyticsManager
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.home.HomeActivity
import com.hmis_tn.doctor.ui.quick_reg.model.labapprovalresult.LabApprovalResultResponse
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.LabTestApprovalRequestModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.LabrapidSaveRequestModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.SendIdList
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.*
import com.hmis_tn.doctor.ui.quick_reg.view_model.LabTestApprovalViewModel
import com.hmis_tn.doctor.ui.quick_reg.view_model.LabTestApprovalViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class LabTestApprovalActivity : Fragment(),
    TestApprovalResultDialogFragment.OnLabTestApprovalActivityRefreshListener,
    RejectDialogFragment.OnLabTestRefreshListener,
    TestApprovalResultDialogFragment.OnLabTestCallBack {
    var binding: ActivityLabApprovalTestBinding? = null
    var utils: Utils? = null
    private var selectTestitemUuid: String? = ""
    private var selectAssignitemUuid: String? = ""
    private var viewModel: LabTestApprovalViewModel? = null
    var appPreferences: AppPreferences? = null
    private var mAdapter: LabTestApprovalAdapter? = null
    private var endDate: String = ""
    private var startDate: String = ""
    var linearLayoutManager: LinearLayoutManager? = null
    private var isLoadingPaginationAdapterCallback: Boolean = false
    private var listfilteritem: ArrayList<LabTestSpinnerresponseContent?>? = ArrayList()
    private var listfilteritemAssignSpinner: ArrayList<LabAssignedToresponseContent?>? = ArrayList()
    private var FilterTestNameResponseMap = mutableMapOf<Int, String>()
    private var FilterAssignSpinnereResponseMap = mutableMapOf<Int, String>()
    /////Pagination

    private var orderId: ArrayList<SendIdList> = ArrayList()


    private var currentPage = 0
    private var pageSize = 10
    var testMethodCode: String = ""
    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES: Int = 0
    var checktestspinner = 0
    var checkassignedToSpinner = 0
    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null
    private var fromDate: String = ""
    private var toDate: String = ""
    private var fromDateRev: String = ""
    private var toDateRev: String = ""
    private var pinOrMobile: String = ""
    private var orderNumber: String = ""
    private var LabUUId: Int? = null

    var cal = Calendar.getInstance()

    var ACCEPTEDUUId: Int = 10

    var APPROVEDUUId: Int = 7

    var EXECUTEDUUId: Int = 13
    var APPROVELPENDINGAUTH: Int = 1

    var REJECTEDUUId: Int = 2

    var SAMPLE_RECEIVE: Int = 14
    var SAMPLE_TRANSPORTUUId: Int = 16
    var SENDFORAPPROVALUUId: Int = 19

    var covid: String = "COVID"
    private var facility_id: Int = 0


    //private var customProgressDialog: CustomProgressDialog? = null
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.activity_lab_approval_test,
                container,
                false
            )

        viewModel = LabTestApprovalViewModelFactory(
            requireActivity().application
        )
            .create(LabTestApprovalViewModel::class.java)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        utils = Utils(requireContext())

        binding?.searchDrawerCardView?.setOnClickListener {
            binding?.drawerLayout!!.openDrawer(GravityCompat.END)
        }
        binding?.drawerLayout?.drawerElevation = 0f
        binding?.drawerLayout?.setScrimColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )
        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding?.labTestApprpovalrecycleview!!.layoutManager = linearLayoutManager
        mAdapter = LabTestApprovalAdapter(requireContext(), ArrayList())
        binding?.labTestApprpovalrecycleview!!.adapter = mAdapter
        appPreferences = AppPreferences.getInstance(
            requireActivity().application,
            AppConstants.SHARE_PREFERENCE_NAME
        )
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        viewModel?.getTextMethod1(facility_id, getTestMethdCallBack1)

        LabUUId = appPreferences?.getInt(AppConstants.LAB_UUID)!!
        //     LabUUId = 39


        /////////////Pagination scrollview
        binding?.labTestApprpovalrecycleview?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (!isLoadingPaginationAdapterCallback) {
                        isLoadingPaginationAdapterCallback = true
                        currentPage += 1
                        if (currentPage <= TOTAL_PAGES) {
                            // Toast.makeText(requireContext(),""+currentPage,Toast.LENGTH_LONG).show()
                            labListSeacondAPI(pageSize, currentPage)
                        }

                    }
                }
            }
        })



        binding?.testSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectTestitemUuid = ""
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    if (++checktestspinner > 1) {
                        currentPage = 0
                        val itemValue = parent!!.getItemAtPosition(pos).toString()
                        if (pos == 0) {

                            selectTestitemUuid = ""
                        } else {
                            selectTestitemUuid =
                                FilterTestNameResponseMap.filterValues { it == itemValue }.keys.toList()[0].toString()
                            Log.e("selectId", selectTestitemUuid.toString())
                        }
                    }


                }
            }

        binding?.assignedToSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectAssignitemUuid = ""
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    if (++checkassignedToSpinner > 1) {
                        currentPage = 0
                        val itemValue = parent!!.getItemAtPosition(pos).toString()
                        if (pos == 0) {

                            selectAssignitemUuid = ""
                        } else {
                            selectAssignitemUuid =
                                FilterAssignSpinnereResponseMap.filterValues { it == itemValue }.keys.toList()[0].toString()
                            Log.e("selectId", selectAssignitemUuid.toString())
                        }

                    }

                }
            }

        binding?.calendarEditText!!.setOnClickListener {

            Toast.makeText(requireContext(), "Select Start Date", Toast.LENGTH_LONG).show()
            val c: Calendar = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    Toast.makeText(requireContext(), "Select To Date", Toast.LENGTH_LONG).show()
                    fromDate = String.format(
                        "%02d",
                        dayOfMonth
                    ) + "-" + String.format("%02d", monthOfYear + 1) + "-" + year

                    fromDateRev = year.toString() + "-" + String.format(
                        "%02d",
                        monthOfYear + 1
                    ) + "-" + String.format(
                        "%02d",
                        dayOfMonth
                    )

                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val dateoickDialog = DatePickerDialog(
                        requireContext(),
                        DatePickerDialog.OnDateSetListener { view, year1, month1, dayOfMonth1 ->

                            toDate = String.format(
                                "%02d",
                                dayOfMonth1
                            ) + "-" + String.format("%02d", month1 + 1) + "-" + year1

                            toDateRev = year1.toString() + "-" + String.format(
                                "%02d",
                                month1 + 1
                            ) + "-" + String.format(
                                "%02d",
                                dayOfMonth1
                            )

                            binding?.calendarEditText!!.setText(fromDate + "-" + toDate)

                        },
                        mYear!!,
                        mMonth!!,
                        mDay!!
                    )

                    dateoickDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis

                    dateoickDialog.datePicker.minDate = cal.timeInMillis

                    dateoickDialog.show()

                }, mYear!!, mMonth!!, mDay!!
            )

            datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
            datePickerDialog.show()

        }

        binding?.searchButton!!.setOnClickListener {

            if (!binding?.calendarEditText!!.text.trim().toString().isEmpty()) {
                startDate = fromDateRev + "T00:01:00.000Z"
                endDate = toDateRev + "T23:59:59.000Z"
            }
            binding?.drawerLayout!!.closeDrawer(GravityCompat.END)

            pinOrMobile = binding?.searchUsingMobileNo!!.text.trim().toString()
            orderNumber = binding?.searchOrderNumber!!.text.trim().toString()

            mAdapter!!.clearAll()

            pageSize = 10

            currentPage = 0

            labTestApprovalListAPI(pageSize, currentPage)


        }

        mAdapter!!.setOnPrintClickListener(object : LabTestApprovalAdapter.OnPrintClickListener {

            override fun onPrintClick(uuid: Int) {

                Log.i("print", "" + uuid)

                val bundle = Bundle()

                bundle.putInt("pdfid", uuid)

                bundle.putString("from", "approval")

                val labtemplatedialog = LabPDF()

                labtemplatedialog.arguments = bundle

                (activity as HomeActivity).replaceFragment(labtemplatedialog)

            }
        })


        binding!!.result.setOnClickListener {

            val request: com.hmis_tn.doctor.ui.quick_reg.model.labapprovalresult.Req =
                com.hmis_tn.doctor.ui.quick_reg.model.labapprovalresult.Req()

            val datas = mAdapter!!.getSelectedCheckData()

            var OrderList: ArrayList<com.hmis_tn.doctor.ui.quick_reg.model.labapprovalresult.OrderProcessDetail> =
                ArrayList()

            OrderList.clear()

            var idList: ArrayList<SendIdList> = ArrayList()

            var status: Boolean = true

            if (datas!!.size != 0) {

                if (datas.size == 1) {

                    for (i in datas.indices) {

                        if (datas[i]!!.order_status_uuid == 7 || datas[i]!!.order_status_uuid == SENDFORAPPROVALUUId) {

                            var order: com.hmis_tn.doctor.ui.quick_reg.model.labapprovalresult.OrderProcessDetail =
                                com.hmis_tn.doctor.ui.quick_reg.model.labapprovalresult.OrderProcessDetail()

                            order.Id = datas[i]!!.uuid!!

                            order.order_status_uuid = datas[i]!!.order_status_uuid!!

                            order.to_location_uuid = datas[i]!!.to_location_uuid!!


                            val reject: SendIdList = SendIdList()

                            reject.Id = datas[i]!!.uuid!!

                            idList.add(reject)

                            if (order.auth_status_uuid != null) {

                                order.auth_status_uuid = datas[i]!!.auth_status_uuid!!

                            }
                            OrderList.add(order)

                        } else {

                            status = false

                        }
                    }

                    if (status) {

                        request.OrderProcessDetails = OrderList

                        orderId = idList

                        testMethodCode = datas[0]!!.test_code!!

                        viewModel!!.orderDetailsGet(request, orderDetailsRetrofitCallback)

                    } else {

                        Toast.makeText(requireContext(), "Cannot process order", Toast.LENGTH_SHORT)
                            .show()
                    }

                } else {


                    if (datas[0]!!.test_code == covid) {

                        for (i in datas.indices) {

                            if (datas[i]!!.order_status_uuid == 7 || datas[i]!!.order_status_uuid == SENDFORAPPROVALUUId) {

                                var order: com.hmis_tn.doctor.ui.quick_reg.model.labapprovalresult.OrderProcessDetail =
                                    com.hmis_tn.doctor.ui.quick_reg.model.labapprovalresult.OrderProcessDetail()

                                order.Id = datas[i]!!.uuid!!

                                order.order_status_uuid = datas[i]!!.order_status_uuid!!

                                order.to_location_uuid = datas[i]!!.to_location_uuid!!

                                if (order.auth_status_uuid != null) {

                                    order.auth_status_uuid = datas[i]!!.auth_status_uuid!!

                                }
                                OrderList.add(order)

                            } else {

                                status = false

                            }
                        }

                        if (status) {

                            testMethodCode = datas[0]!!.test_code!!

                            request.OrderProcessDetails = OrderList

                            viewModel!!.orderDetailsGet(request, orderDetailsRetrofitCallback)

                        } else {

                            Toast.makeText(
                                requireContext(),
                                "Cannot process order",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Only COVID test allowed for multiple Approval",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }

            } else {

                Toast.makeText(requireContext(), "Please Select Any one Item", Toast.LENGTH_SHORT)
                    .show()

            }


        }


        binding?.clear!!.setOnClickListener {

            clearSearch()

        }


        /*
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        startDate = utils!!.getAgeMonth(1)
        endDate = sdf.format(Date())*/

        val sdf = SimpleDateFormat("yyyy-MM-dd")

        val formatter = SimpleDateFormat("dd-MM-yyyy")

        binding?.calendarEditText!!.setText("""${formatter.format(Date())}-${formatter.format(Date())}""")

        startDate = utils!!.getAgedayDifferent(1) + "T18:30:00.000Z"

        endDate = sdf.format(Date()) + "T18:29:59.000Z"

        binding?.rejected?.setOnClickListener {

            val datas = mAdapter!!.getSelectedCheckData()

            var reqest: LabrapidSaveRequestModel = LabrapidSaveRequestModel()

            var detailsArray: ArrayList<SendIdList> = ArrayList()

            detailsArray.clear()

            var status: Boolean = true

            if (datas!!.size != 0) {

                for (i in datas.indices) {


                    if ((datas[i]!!.order_status_uuid == 7 || datas[i]!!.order_status_uuid == SENDFORAPPROVALUUId)) {


                        val details: SendIdList = SendIdList()

                        details.Id = datas[i]!!.uuid!!

                        detailsArray.add(details)

                    } else {

                        status = false

                    }
                }

                if (status) {

                    val ft = childFragmentManager.beginTransaction()
                    val dialog = RejectDialogFragment()
                    val bundle = Bundle()

                    bundle.putParcelableArrayList(AppConstants.RESPONSECONTENT, detailsArray)
                    dialog.arguments = bundle
                    dialog.show(ft, "Tag")


                } else {

                    Toast.makeText(requireContext(), "Cannot process order", Toast.LENGTH_SHORT)
                        .show()

                }

            } else {

                Toast.makeText(requireContext(), "Please Select Any one Item", Toast.LENGTH_SHORT)
                    .show()

            }


        }

        binding!!.selectAllCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->

            mAdapter!!.selectAllCheckbox(isChecked)

        }
        labTestApprovalListAPI(pageSize, currentPage)


        return binding!!.root
    }


    private fun labListSeacondAPI(pageSize: Int, currentPage: Int) {

        val labTestApprovalRequestModel = LabTestApprovalRequestModel()

        labTestApprovalRequestModel.pageNo = currentPage
        labTestApprovalRequestModel.paginationSize = pageSize
        labTestApprovalRequestModel.search = ""
        labTestApprovalRequestModel.test_name = selectTestitemUuid
        labTestApprovalRequestModel.to_facility_name = ""
        labTestApprovalRequestModel.from_facility_uuid = selectAssignitemUuid
        labTestApprovalRequestModel.order_number = orderNumber
        labTestApprovalRequestModel.fromDate = startDate
        labTestApprovalRequestModel.toDate = endDate
        if (LabUUId != 0) {
            labTestApprovalRequestModel.lab_uuid = LabUUId.toString()
            labTestApprovalRequestModel.to_location_uuid = LabUUId.toString()
        }
        labTestApprovalRequestModel.sortField = "order_status_uuid"
        labTestApprovalRequestModel.sortOrder = "DESC"
        val arrayList: ArrayList<Int> = ArrayList()
        arrayList.add(19)
        arrayList.add(7)
        arrayList.add(2)
        arrayList.add(9)
        arrayList.add(2)
        labTestApprovalRequestModel.order_status_uuids = arrayList
        labTestApprovalRequestModel.is_approved_required = 1
        labTestApprovalRequestModel.is_requied_test_approval_list = true

        labTestApprovalRequestModel.pinOrMobile = pinOrMobile
        labTestApprovalRequestModel.widget_filter = ""
        labTestApprovalRequestModel.qualifier_filter = ""
        labTestApprovalRequestModel.auth_status_uuid = ""

        val labtest2 = Gson().toJson(labTestApprovalRequestModel)

        Log.i("", "" + labtest2)

        viewModel?.getLabTestApprovalListSecond(
            labTestApprovalRequestModel,
            labTestApprovalResponseSecondRetrofitCallback
        )


    }


    private fun labTestApprovalListAPI(pageSize: Int, currentPage: Int) {

        val labTestApprovalRequestModel = LabTestApprovalRequestModel()

        labTestApprovalRequestModel.pageNo = currentPage
        labTestApprovalRequestModel.paginationSize = pageSize
        labTestApprovalRequestModel.search = ""
        labTestApprovalRequestModel.test_name = selectTestitemUuid
        labTestApprovalRequestModel.to_facility_name = ""
        labTestApprovalRequestModel.from_facility_uuid = selectAssignitemUuid
        labTestApprovalRequestModel.order_number = orderNumber
        labTestApprovalRequestModel.fromDate = startDate
        labTestApprovalRequestModel.toDate = endDate
        if (LabUUId != 0) {
            labTestApprovalRequestModel.lab_uuid = LabUUId.toString()
            labTestApprovalRequestModel.to_location_uuid = LabUUId.toString()
        }
        labTestApprovalRequestModel.sortField = "order_status_uuid"
        labTestApprovalRequestModel.sortOrder = "DESC"

        val arrayList: ArrayList<Int> = ArrayList()
        arrayList.add(19)
        arrayList.add(7)
        arrayList.add(2)
        arrayList.add(9)
        arrayList.add(2)
        labTestApprovalRequestModel.order_status_uuids = arrayList
        labTestApprovalRequestModel.is_approved_required = 1
        labTestApprovalRequestModel.is_requied_test_approval_list = true

        labTestApprovalRequestModel.pinOrMobile = pinOrMobile
        labTestApprovalRequestModel.widget_filter = ""
        labTestApprovalRequestModel.qualifier_filter = ""
        labTestApprovalRequestModel.auth_status_uuid = ""

        var labtest1 = Gson().toJson(labTestApprovalRequestModel)

        Log.i("", "" + labtest1)
        Log.i("", "" + labtest1)
        Log.i("", "" + labtest1)
        Log.i("", "" + labtest1)


        viewModel?.getLabTestApprovalList(
            labTestApprovalRequestModel,
            labTestApprovalResponseRetrofitCallback
        )

    }

    val labTestApprovalResponseRetrofitCallback = object :
        RetrofitCallback<LabTestApprovalResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LabTestApprovalResponseModel>?) {
            Log.e("labTestResponse", responseBody?.body()?.responseContents.toString())

            var responsedata = Gson().toJson(responseBody?.body()?.responseContents)

            Log.i("", "" + responsedata)
            Log.i("", "" + responsedata)
            Log.i("", "" + responsedata)
            Log.i("", "" + responsedata)



            binding?.positiveTxt!!.text = "0"

            binding?.negativeTxt!!.text = "0"

            binding?.equivocalTxt!!.text = "0"

            binding?.rejectedTxt!!.text = "0"

            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {


                Log.i(
                    "page",
                    "" + currentPage + " " + responseBody.body()?.responseContents!!.size
                )
                TOTAL_PAGES =
                    Math.ceil(responseBody.body()!!.totalRecords!!.toDouble() / 10).toInt()

                if (responseBody.body()!!.responseContents!!.isNotEmpty()) {
                    isLoadingPaginationAdapterCallback = false
                    mAdapter!!.addAll(responseBody.body()!!.responseContents)
                    if (currentPage < TOTAL_PAGES) {
                        binding?.progressbar!!.visibility = View.VISIBLE
                        mAdapter!!.addLoadingFooter()
                        isLoading = true
                        isLastPage = false
                    } else {
                        binding?.progressbar!!.visibility = View.GONE
                        mAdapter!!.removeLoadingFooter()
                        isLoading = false
                        isLastPage = true
                    }

                } else {
                    binding?.progressbar!!.visibility = View.GONE
                    mAdapter!!.removeLoadingFooter()
                    isLoading = false
                    isLastPage = true
                }
                if (responseBody.body()!!.totalRecords!! < 11) {
                    binding?.progressbar!!.visibility = View.GONE
                }

                val diseaseList = responseBody.body()?.disease_result_data
                for (i in diseaseList!!.indices) {

                    /*if(diseaseList.size != 0 && diseaseList[i]?.qualifier_uuid == 2){
                        binding?.positiveTxt!!.setText(diseaseList[i]?.qualifier_count.toString())
                    }else if(diseaseList.size != 0 && diseaseList[i]?.qualifier_uuid == 1){
                        binding?.negativeTxt!!.setText(diseaseList[i]?.qualifier_count.toString())
                    }else if(diseaseList.size != 0 && diseaseList[i]?.qualifier_uuid == 3){
                        binding?.equivocalTxt!!.setText(diseaseList[i]?.qualifier_count.toString())
                    }*/

                    if (diseaseList[i]?.auth_status_uuid == 1) {

                        binding?.negativeTxt!!.text = diseaseList[i]?.qualifier_count.toString()

                    }
                }

                val orderList = responseBody.body()?.order_status_count

                if (orderList?.size != 0) {

                    for (i in orderList!!.indices) {

                        if (orderList[i]?.order_status_uuid == 2) {

                            binding?.rejectedTxt!!.text = orderList[i]?.order_count.toString()

                        }
                        if (orderList[i]?.order_status_uuid == SENDFORAPPROVALUUId) {

                            binding?.positiveTxt!!.text = orderList[i]?.order_count.toString()

                        }


                    }

                }

            } else {
                Toast.makeText(requireContext(), "No records found", Toast.LENGTH_LONG).show()
                binding?.progressbar!!.visibility = View.GONE

                binding?.positiveTxt!!.text = "0"

                binding?.negativeTxt!!.text = "0"

                binding?.equivocalTxt!!.text = "0"

                binding?.rejectedTxt!!.text = "0"
                mAdapter!!.clearAll()
            }

        }

        override fun onBadRequest(errorBody: Response<LabTestApprovalResponseModel>?) {
            isLoadingPaginationAdapterCallback = false
            val gson = GsonBuilder().create()
            val responseModel: LabTestApprovalResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    LabTestApprovalResponseModel::class.java
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
            isLoadingPaginationAdapterCallback = false
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            isLoadingPaginationAdapterCallback = false
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
            isLoadingPaginationAdapterCallback = false
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }

    }


    val labTestApprovalResponseSecondRetrofitCallback =
        object : RetrofitCallback<LabTestApprovalResponseModel> {
            override fun onSuccessfulResponse(response: Response<LabTestApprovalResponseModel>) {
                if (response.body()?.responseContents!!.isNotEmpty()) {

                    mAdapter!!.removeLoadingFooter()
                    isLoadingPaginationAdapterCallback = false
                    isLoading = false
                    mAdapter?.addAll(response.body()!!.responseContents)
                    Log.i(
                        "page",
                        "" + currentPage + " " + response.body()?.responseContents!!.size
                    )
                    println("testing for two  = $currentPage--$TOTAL_PAGES")
                    binding?.progressbar!!.visibility = View.GONE

                    if (currentPage < TOTAL_PAGES) {
                        binding?.progressbar!!.visibility = View.VISIBLE
                        mAdapter?.addLoadingFooter()
                        isLoading = true
                        isLastPage = false
                        println("testing for four  = $currentPage--$TOTAL_PAGES")
                    } else {
                        isLastPage = true
                        binding?.progressbar!!.visibility = View.GONE
//                    visitHistoryAdapter.removeLoadingFooter()
                        isLoading = false
                        isLastPage = true
                        println("testing for five  = $currentPage--$TOTAL_PAGES")
                    }


                } else {
                    println("testing for six  = $currentPage--$TOTAL_PAGES")
                    binding?.progressbar!!.visibility = View.GONE
                    mAdapter?.removeLoadingFooter()
                    isLoading = false
                    isLastPage = true
                }


            }

            override fun onBadRequest(response: Response<LabTestApprovalResponseModel>?) {
                isLoadingPaginationAdapterCallback = false
                mAdapter?.removeLoadingFooter()
                isLoading = false
                isLastPage = true

            }

            override fun onServerError(response: Response<*>) {
                viewModel!!.progress.value = View.GONE
                utils!!.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                viewModel!!.progress.value = View.GONE
                utils!!.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onForbidden() {
                viewModel!!.progress.value = View.GONE
                utils!!.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }


    val getTestMethdCallBack1 =
        object : RetrofitCallback<LabTestSpinnerResponseModel> {

            override fun onSuccessfulResponse(response: Response<LabTestSpinnerResponseModel>) {
                Log.i("", "" + response.body()?.responseContents)

                listfilteritem?.add(LabTestSpinnerresponseContent())
                listfilteritem?.addAll(response.body()?.responseContents!!)

                FilterTestNameResponseMap =
                    listfilteritem!!.map { it?.uuid!! to it.name!! }.toMap().toMutableMap()

                try {
                    val adapter =
                        ArrayAdapter<String>(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            FilterTestNameResponseMap.values.toMutableList()
                        )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding?.testSpinner!!.adapter = adapter
                } catch (e: Exception) {

                }
                binding?.testSpinner?.prompt = listfilteritem?.get(0)?.name
                binding?.testSpinner?.setSelection(0)

                viewModel?.getTextAssignedTo(facility_id, LabAssignedSpinnerRetrofitCallback)
            }

            override fun onBadRequest(response: Response<LabTestSpinnerResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: LabTestSpinnerResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        LabTestSpinnerResponseModel::class.java
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


    val LabAssignedSpinnerRetrofitCallback = object : RetrofitCallback<LabAssignedToResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LabAssignedToResponseModel>?) {

            Log.e("AssignedToSpinner", responseBody?.body()?.responseContents.toString())

            listfilteritemAssignSpinner?.add(LabAssignedToresponseContent())
            listfilteritemAssignSpinner?.addAll(responseBody!!.body()?.responseContents!!)

            FilterAssignSpinnereResponseMap =
                listfilteritemAssignSpinner!!.map { it?.uuid!! to it.name!! }.toMap()
                    .toMutableMap()

            try {
                val adapter =
                    ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        FilterAssignSpinnereResponseMap.values.toMutableList()
                    )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding?.assignedToSpinner!!.adapter = adapter
            } catch (e: Exception) {

            }
            binding?.assignedToSpinner?.prompt = listfilteritem?.get(0)?.name
            binding?.assignedToSpinner?.setSelection(0)

        }

        override fun onBadRequest(response: Response<LabAssignedToResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: LabAssignedToResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    LabAssignedToResponseModel::class.java
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

    val orderDetailsRetrofitCallback = object : RetrofitCallback<LabApprovalResultResponse> {
        override fun onSuccessfulResponse(responseBody: Response<LabApprovalResultResponse>?) {

            //  labListAPI()


            val responsearray = responseBody!!.body()!!.responseContents.rows


            var status: Boolean = true

            if (testMethodCode == covid) {

                val resultvalue = responsearray[0].qualifier_uuid

                for (i in responsearray.indices) {

                    if (resultvalue != responsearray[i].qualifier_uuid) {
                        status = false

                    }


                }

            }


            Log.i("order", "Save" + responseBody.body()!!.responseContents.rows)

            val ft = childFragmentManager.beginTransaction()

            if (status) {

                val dialog = TestApprovalResultDialogFragment()

                val bundle = Bundle()

                val saveArray = responseBody.body()!!.responseContents.rows

                bundle.putParcelableArrayList(AppConstants.RESPONSECONTENT, saveArray)

                bundle.putParcelableArrayList(AppConstants.RESPONSEORDERARRAY, orderId)

                bundle.putString("testMethodCode", testMethodCode)

                dialog.arguments = bundle

                dialog.show(ft, "Tag")

            } else {

                Toast.makeText(
                    context,
                    "Cannot be Process Test result value are not same",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        override fun onBadRequest(errorBody: Response<LabApprovalResultResponse>?) {
            val gson = GsonBuilder().create()
            val responseModel: LabApprovalResultResponse
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    LabApprovalResultResponse::class.java
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

    private fun clearSearch() {

        binding?.calendarEditText!!.setText("")
        binding?.searchUsingMobileNo!!.setText("")
        binding?.searchOrderNumber!!.setText("")
        binding?.assignedToSpinner?.setSelection(0)
        binding?.testSpinner?.setSelection(0)

    }


    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is RejectDialogFragment) {
            childFragment.setOnLabTestRefreshListener(this)
        }
        if (childFragment is TestApprovalResultDialogFragment) {
            childFragment.setOnLabTestApprovalRefreshListener(this)
        }

        if (childFragment is TestApprovalResultDialogFragment) {
            childFragment.setOnLabTestProcess(this)
        }

    }

    override fun onRefreshList() {
        Toast.makeText(requireContext(), "Rejected Successfully", Toast.LENGTH_LONG).show()

        mAdapter!!.clearAll()

        pageSize = 10

        currentPage = 0

        labTestApprovalListAPI(10, 0)
    }

    override fun onRefreshLabTestApprovalList() {

        AnalyticsManager.getAnalyticsManager()
            .trackLMISLabApprovalOrderApproval(requireContext(), "")

        mAdapter!!.clearAll()

        pageSize = 10

        currentPage = 0

        labTestApprovalListAPI(10, 0)

    }

    override fun onArrayList(orderData: ArrayList<SendIdList>?) {


        val ft = childFragmentManager.beginTransaction()
        val dialog = RejectDialogFragment()
        val bundle = Bundle()
        bundle.putParcelableArrayList(AppConstants.RESPONSECONTENT, orderData)
        dialog.arguments = bundle
        dialog.show(ft, "Tag")

    }


}




