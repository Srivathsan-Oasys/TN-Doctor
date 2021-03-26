package com.hmis_tn.doctor.ui.quick_reg.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
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
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.ActivityLabTestProcessBinding
import com.hmis_tn.doctor.fire_base_analytics.AnalyticsManager
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.radiology.model.RecyclerDto
import com.hmis_tn.doctor.ui.home.HomeActivity
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.LabrapidSaveRequestModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.SendIdList
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.TestProcessRequestModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.orderRequest.OrderProcessDetail
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.orderRequest.OrderProcessDetailsResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.orderRequest.Req
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.*
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.testprocess.TestProcessResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.testprocess.sampleTransportRequestModel
import com.hmis_tn.doctor.ui.quick_reg.view_model.LabTestProcessViewModel
import com.hmis_tn.doctor.ui.quick_reg.view_model.LabTestProcessViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LabTestProcessActivity : Fragment(), RejectDialogFragment.OnLabTestRefreshListener,
    OrderProcessDialogFragment.OnOrderProcessListener,
    SendForApprovalDialogFragment.OnsendForApprovalListener,
    AssignToOtherDialogFragment.OnAssignToOtherListener,
    OrderProcessDialogFragment.OnLabTestCallBack {
    var binding: ActivityLabTestProcessBinding? = null
    var utils: Utils? = null
    private var endDate: String = ""
    private var startDate: String = ""
    private var viewModel: LabTestProcessViewModel? = null
    var appPreferences: AppPreferences? = null
    private var mAdapter: LabTestsProcessAdapter? = null
    private val labTestList: MutableList<RecyclerDto> = ArrayList()
    private var selectAssignitemUuid: String? = ""
    private var selectTestitemUuid: String? = ""
    var linearLayoutManager: LinearLayoutManager? = null
    private var facility_id: Int = 0
    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null
    private var fromDate: String = ""
    private var toDate: String = ""
    private var fromDateRev: String = ""
    private var toDateRev: String = ""
    var checktestspinner = 0
    var checkassignedToSpinner = 0
    private var isLoadingPaginationAdapterCallback: Boolean = false
    var cal = Calendar.getInstance()
    private var LabUUId: Int? = null

    var testMethodCode: String = ""

    private var orderId: ArrayList<SendIdList> = ArrayList()

    var ACCEPTEDUUId: Int = 10


    var APPROVEDUUId: Int = 7

    var EXECUTEDUUId: Int = 13

    var REJECTEDUUId: Int = 2

    var SAMPLE_RECEIVE: Int = 14

    var SAMPLE_IN_TRANSPORTUUId: Int = 16

    var SAMPLE_TRANSPORTUUId: Int = 15

    var APPROVELPENDINGAUTHID: Int = 1

    var SENDFORAPPROVALUUId: Int = 19

    private var currentPage = 0
    private var pageSize = 10
    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES: Int = 0

    private var orderCount: Int = 0

    private var pinOrMobile: String = ""
    private var orderNumber: String = ""

    private var listfilteritem: ArrayList<LabTestSpinnerresponseContent?>? = ArrayList()
    private var listfilteritemAssignSpinner: ArrayList<LabAssignedToresponseContent?>? = ArrayList()
    private var FilterTestNameResponseMap = mutableMapOf<Int, String>()
    private var FilterAssignSpinnereResponseMap = mutableMapOf<Int, String>()


    //private var customProgressDialog: CustomProgressDialog? = null
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.activity_lab_test_process,
                container,
                false
            )

        viewModel = LabTestProcessViewModelFactory(
            requireActivity().application
        )
            .create(LabTestProcessViewModel::class.java)
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
        binding?.labTestProcessrecycleview!!.layoutManager = linearLayoutManager
        mAdapter = LabTestsProcessAdapter(requireContext(), ArrayList())
        binding?.labTestProcessrecycleview!!.adapter = mAdapter

        appPreferences = AppPreferences.getInstance(
            requireActivity().application,
            AppConstants.SHARE_PREFERENCE_NAME
        )
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        viewModel?.getTextMethod1(facility_id, getTestMethdCallBack1)
        LabUUId = appPreferences?.getInt(AppConstants.LAB_UUID)!!

        binding?.result?.setOnClickListener {

            val dates = mAdapter!!.getSelectedCheckData()


            val RequestModel: sampleTransportRequestModel = sampleTransportRequestModel()

            val request: ArrayList<Int> = ArrayList()

            var status: Boolean = true

            if (dates!!.size != 0) {

                for (i in dates!!.indices) {

                    if (dates[i]!!.test_method_uuid != 2 && dates[i]!!.order_status_uuid == SAMPLE_IN_TRANSPORTUUId) {

                        request.add(dates[i]!!.sample_transport_details_uuid!!)
                    } else {

                        status = false

                    }
                }

                if (status) {

                    RequestModel.sample_transport_details_uuid_s = request

                    viewModel!!.sampleRecived(RequestModel, sampleRecivedRetrofitCallback)

                    binding?.progressbar!!.setVisibility(View.VISIBLE);


                } else {

                    Toast.makeText(
                        context,
                        "Cannot be process Sample in transport Status only allowed",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            } else {

                Toast.makeText(requireContext(), "Please Select Any one Item", Toast.LENGTH_SHORT)
                    .show()

            }


        }

        binding!!.selectAllCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->

            mAdapter!!.selectAllCheckboxes(isChecked)

        }


        mAdapter!!.setOnPrintClickListener(object : LabTestsProcessAdapter.OnPrintClickListener {
            override fun onPrintClick(uuid: Int) {


                val bundle = Bundle()

                bundle.putInt("pdfid", uuid)

                bundle.putString("from", "process")

                val labtemplatedialog = LabPDF()

                labtemplatedialog.arguments = bundle

                (activity as HomeActivity).replaceFragment(labtemplatedialog)

            }
        })


        /*
          val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
          startDate = utils!!.getAgeMonth(1)
          endDate = sdf.format(Date())*/

        val sdf = SimpleDateFormat("yyyy-MM-dd")

        val formatter = SimpleDateFormat("dd-MM-yyyy")

        binding?.calendarEditText!!.setText("""${formatter.format(Date())}-${formatter.format(Date())}""")

        startDate = utils!!.getAgedayDifferent(1) + "T23:59:59.000Z"

        endDate = sdf.format(Date()) + "T23:59:59.000Z"



        binding?.labTestProcessrecycleview?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (!isLoadingPaginationAdapterCallback) {
                        isLoadingPaginationAdapterCallback = true
                        currentPage += 1
                        if (currentPage <= TOTAL_PAGES) {
                            // Toast.makeText(requireContext(),""+currentPage,Toast.LENGTH_LONG).show()
                            getTestProcessAPISecond(pageSize, currentPage)
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
                                FilterTestNameResponseMap.filterValues { it == itemValue }.keys.toList()[0]?.toString()

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
                                FilterAssignSpinnereResponseMap.filterValues { it == itemValue }.keys.toList()[0]?.toString()

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

            getTestProcessAPI(pageSize, currentPage)

        }

        binding!!.order.setOnClickListener {

            val request: Req = Req()

            val datas = mAdapter!!.getSelectedCheckData()


            orderCount = datas!!.size

            var OrderList: ArrayList<OrderProcessDetail> = ArrayList()

            OrderList.clear()

            var idList: ArrayList<SendIdList> = ArrayList()

            var status: Boolean = true

            if (datas!!.size != 0) {

                val testid = datas[0]!!.test_master_uuid

                for (i in datas!!.indices) {

                    if (datas[i]!!.test_master_uuid == testid && (datas[i]!!.order_status_uuid == SAMPLE_RECEIVE || datas[i]!!.order_status_uuid == ACCEPTEDUUId)) {

                        var order: OrderProcessDetail = OrderProcessDetail()

                        val reject: SendIdList = SendIdList()

                        reject.Id = datas[i]!!.uuid!!

                        idList.add(reject)

                        order.Id = datas[i]!!.uuid!!

                        order.order_status_uuid = datas[i]!!.order_status_uuid!!

                        order.to_location_uuid = datas[i]!!.to_location_uuid!!

                        OrderList.add(order)

                    } else {

                        status = false

                    }
                }

                if (status) {

                    request.OrderProcessDetails = OrderList

                    testMethodCode = datas[0]!!.test_code!!

                    orderId = idList

                    viewModel!!.orderDetailsGet(request, orderDetailsRetrofitCallback)

                } else {

                    Toast.makeText(
                        context,
                        "Cannot be process Sample Receive and Executed Status only allowed",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            } else {

                Toast.makeText(requireContext(), "Please Select Any one Item", Toast.LENGTH_SHORT)
                    .show()

            }

        }

        binding!!.saveOfApproval.setOnClickListener {

            val datas = mAdapter!!.getSelectedCheckData()

            var reqest: LabrapidSaveRequestModel = LabrapidSaveRequestModel()

            var detailsArray: ArrayList<SendIdList> = ArrayList()

            detailsArray.clear()

            var status: Boolean = true

            if (datas!!.size != 0) {

                for (i in datas!!.indices) {

                    if (datas[i]!!.test_method_uuid != 2 && datas[i]!!.order_status_uuid == EXECUTEDUUId) {

                        val details: SendIdList = SendIdList()

                        details.Id = datas[i]!!.uuid!!

                        detailsArray.add(details)

                    } else {

                        status = false

                    }
                }

                if (status) {


                    val ft = childFragmentManager.beginTransaction()
                    val dialog = SendForApprovalDialogFragment()
                    val bundle = Bundle()

                    bundle.putParcelableArrayList(AppConstants.RESPONSECONTENT, detailsArray)
                    dialog.arguments = bundle
                    dialog.show(ft, "Tag")


                } else {

                    Toast.makeText(
                        context,
                        "Cannot send for approval, Please check the status! EXECUTED Status only allowed",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            } else {

                Toast.makeText(requireContext(), "Please Select Any one Item", Toast.LENGTH_SHORT)
                    .show()

            }


        }


        binding?.clear!!.setOnClickListener {

            clearSearch()

        }

        binding?.rejected?.setOnClickListener {

            val datas = mAdapter!!.getSelectedCheckData()

            var reqest: LabrapidSaveRequestModel = LabrapidSaveRequestModel()

            var detailsArray: ArrayList<SendIdList> = ArrayList()

            detailsArray.clear()

            var status: Boolean = true

            if (datas!!.size != 0) {

                for (i in datas!!.indices) {

                    if ((datas[i]!!.order_status_uuid == SAMPLE_RECEIVE || datas[i]!!.order_status_uuid == SAMPLE_TRANSPORTUUId) || (datas[i]!!.order_status_uuid == EXECUTEDUUId || datas[i]!!.order_status_uuid == SAMPLE_RECEIVE)) {

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

        binding?.assignOthers?.setOnClickListener {

            val datas = mAdapter!!.getSelectedCheckData()

            var reqest: LabrapidSaveRequestModel = LabrapidSaveRequestModel()

            var detailsArray: ArrayList<SendIdList> = ArrayList()

            detailsArray.clear()

            var status: Boolean = true

            var testSinlgeStatus: Boolean? = true

            if (datas!!.size != 0) {

                var firstId = datas?.get(0)!!.test_master_uuid

                for (i in datas!!.indices) {

                    if (datas[i]!!.test_method_uuid != 2 && (datas[i]!!.order_status_uuid == SAMPLE_RECEIVE || datas[i]!!.order_status_uuid == SAMPLE_IN_TRANSPORTUUId)) {

                        val details: SendIdList = SendIdList()

                        details.Id = datas[i]!!.uuid!!

                        if (datas[i]!!.test_name != null && datas[i]!!.test_name != "") {

                            details.name = datas[i]!!.test_name!!
                        }

                        detailsArray.add(details)


                        if (datas[i]!!.test_master_uuid != firstId) {
                            testSinlgeStatus = false
                        }

                    } else {

                        status = false

                    }
                }

                if (status) {


                    val ft = childFragmentManager.beginTransaction()
                    val dialog = AssignToOtherDialogFragment()
                    val bundle = Bundle()

                    bundle.putParcelableArrayList(AppConstants.RESPONSECONTENT, detailsArray)
                    bundle.putBoolean(AppConstants.RESPONSENEXT, testSinlgeStatus!!)
                    dialog.arguments = bundle
                    dialog.show(ft, "Tag")

                } else {

                    Toast.makeText(
                        context,
                        "Cannot be process Sample receive and Sample in transport Status only allowed",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            } else {

                Toast.makeText(requireContext(), "Please Select Any one Item", Toast.LENGTH_SHORT)
                    .show()

            }

        }

        getTestProcessAPI(pageSize, currentPage)


        return binding!!.root
    }

    private fun getTestProcessAPISecond(pageSize: Int, currentPage: Int) {
        val testProcessRequestModel = TestProcessRequestModel()

        testProcessRequestModel.pageNo = currentPage
        testProcessRequestModel.paginationSize = pageSize
        testProcessRequestModel.search = ""
        testProcessRequestModel.test_name = selectTestitemUuid
        testProcessRequestModel.to_facility_name = selectAssignitemUuid
        testProcessRequestModel.order_number = orderNumber
        testProcessRequestModel.fromDate = startDate
        testProcessRequestModel.toDate = endDate
        testProcessRequestModel.is_requied_test_process_list = true
        if (LabUUId != 0) {
            testProcessRequestModel.lab_uuid = LabUUId.toString()
            testProcessRequestModel.to_location_uuid = LabUUId.toString()
        }
        val arrayList: ArrayList<Int> = ArrayList()
        arrayList.add(14)
        arrayList.add(15)
        arrayList.add(16)
        arrayList.add(13)
        arrayList.add(19)
        arrayList.add(7)
        arrayList.add(2)
        testProcessRequestModel.order_status_uuids = arrayList

        testProcessRequestModel.pinOrMobile = pinOrMobile
        testProcessRequestModel.qualifier_filter = ""
        testProcessRequestModel.widget_filter = ""


        viewModel?.getLabTestProcessListSecond(
            testProcessRequestModel,
            testProcessSecondRetrofitCallback
        )
    }


    private fun getTestProcessAPI(pageSize: Int, currentPage: Int) {

        val testProcessRequestModel = TestProcessRequestModel()

        testProcessRequestModel.pageNo = currentPage
        testProcessRequestModel.paginationSize = pageSize
        testProcessRequestModel.search = ""
        testProcessRequestModel.test_name = selectTestitemUuid
        testProcessRequestModel.to_facility_name = selectAssignitemUuid
        testProcessRequestModel.order_number = orderNumber
        testProcessRequestModel.fromDate = startDate
        testProcessRequestModel.toDate = endDate
        testProcessRequestModel.is_requied_test_process_list = true
        if (LabUUId != 0) {
            testProcessRequestModel.lab_uuid = LabUUId.toString()
            testProcessRequestModel.to_location_uuid = LabUUId.toString()
        }
        val arrayList: ArrayList<Int> = ArrayList()
        arrayList.add(14)
        arrayList.add(15)
        arrayList.add(16)
        arrayList.add(13)
        arrayList.add(19)
        arrayList.add(7)
        arrayList.add(2)
        testProcessRequestModel.order_status_uuids = arrayList
        testProcessRequestModel.pinOrMobile = pinOrMobile
        testProcessRequestModel.qualifier_filter = ""
        testProcessRequestModel.widget_filter = ""



        viewModel?.getLabTestProcessList(testProcessRequestModel, testProcessRetrofitCallback)

    }

    val testProcessRetrofitCallback = object : RetrofitCallback<TestProcessResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<TestProcessResponseModel>?) {


            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
//                viewModel?.errorTextVisibility?.value = 8


                TOTAL_PAGES =
                    Math.ceil(responseBody!!.body()!!.totalRecords!!.toDouble() / 10).toInt()


                if (responseBody.body()!!.responseContents!!.isNotEmpty()!!) {
                    isLoadingPaginationAdapterCallback = false
                    mAdapter!!.addAll(responseBody!!.body()!!.responseContents)



                    binding?.positiveTxt!!.setText("0")

                    binding?.negativeTxt!!.setText("0")

                    binding?.equivocalTxt!!.setText("0")

                    binding?.rejectedTxt!!.setText("0")

                    if (currentPage < TOTAL_PAGES!!) {
                        binding?.progressbar!!.setVisibility(View.VISIBLE);
                        mAdapter!!.addLoadingFooter()
                        isLoading = true
                        isLastPage = false
                    } else {
                        binding?.progressbar!!.setVisibility(View.GONE);
                        mAdapter!!.removeLoadingFooter()
                        isLoading = false
                        isLastPage = true
                    }

                    if (responseBody!!.body()!!.totalRecords!! < 11) {
                        binding?.progressbar!!.setVisibility(View.GONE);
                    }

                    val diseaseList = responseBody?.body()?.disease_result_data
                    for (i in diseaseList!!.indices) {

                        /*    if(diseaseList.isNotEmpty() && diseaseList[i]?.qualifier_uuid == 2){
                                binding?.positiveTxt!!.setText(diseaseList[i]?.qualifier_count.toString())
                            }else if(diseaseList.isNotEmpty() && diseaseList[i]?.qualifier_uuid == 1){
                                binding?.negativeTxt!!.setText(diseaseList[i]?.qualifier_count.toString())
                            }else if(diseaseList.isNotEmpty() && diseaseList[i]?.qualifier_uuid == 3){
                                binding?.equivocalTxt!!.setText(diseaseList[i]?.qualifier_count.toString())
                            }*/
                    }


                    val orderList = responseBody?.body()?.order_status_count

                    if (orderList?.size != 0) {

                        for (i in orderList!!.indices) {

                            if (orderList[i]?.order_status_uuid == 2) {

                                binding?.rejectedTxt!!.setText(orderList[i]?.order_count.toString())

                            }
                            if (orderList[i]?.order_status_uuid == SAMPLE_RECEIVE) {

                                binding?.positiveTxt!!.setText(orderList[i]?.order_count.toString())

                            }
                            if (orderList[i]?.order_status_uuid == SAMPLE_IN_TRANSPORTUUId) {

                                binding?.negativeTxt!!.setText(orderList[i]?.order_count.toString())

                            }
                            if (orderList[i]?.order_status_uuid == SAMPLE_TRANSPORTUUId) {

                                binding?.equivocalTxt!!.setText(orderList[i]?.order_count.toString())

                            }


                        }

                    }

                } else {
                    binding?.progressbar!!.setVisibility(View.GONE);
                    mAdapter!!.removeLoadingFooter()
                    isLoading = false
                    isLastPage = true
                }
            } else {
                Toast.makeText(requireContext(), "No records found", Toast.LENGTH_LONG).show()

                binding?.progressbar!!.setVisibility(View.GONE);

                binding?.positiveTxt!!.setText("0")

                binding?.negativeTxt!!.setText("0")

                binding?.equivocalTxt!!.setText("0")

                binding?.rejectedTxt!!.setText("0")
                mAdapter!!.clearAll()
            }

        }

        override fun onBadRequest(errorBody: Response<TestProcessResponseModel>?) {
            isLoadingPaginationAdapterCallback = false
            val gson = GsonBuilder().create()
            val responseModel: TestProcessResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    TestProcessResponseModel::class.java
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

    val orderDetailsRetrofitCallback = object : RetrofitCallback<OrderProcessDetailsResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<OrderProcessDetailsResponseModel>?) {

            //  labListAPI()


            val ft = childFragmentManager.beginTransaction()
            val dialog = OrderProcessDialogFragment()
            val bundle = Bundle()
            val saveArray = responseBody!!.body()!!.responseContents.rows
            //val reqsize=responseBody!!.body()!!.req.OrderProcessDetails.s
            bundle.putParcelableArrayList(AppConstants.RESPONSECONTENT, saveArray)
            bundle.putInt(AppConstants.RESPONSENEXT, orderCount)
            bundle.putParcelableArrayList(AppConstants.RESPONSEORDERARRAY, orderId)

            bundle.putString("testMethodCode", testMethodCode)

            bundle.putString("From", "Process")



            dialog.arguments = bundle
            dialog.show(ft, "Tag")

        }

        override fun onBadRequest(errorBody: Response<OrderProcessDetailsResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: OrderProcessDetailsResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    OrderProcessDetailsResponseModel::class.java
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


    val testProcessSecondRetrofitCallback = object : RetrofitCallback<TestProcessResponseModel> {
        override fun onSuccessfulResponse(response: Response<TestProcessResponseModel>) {
            if (response.body()?.responseContents!!.isNotEmpty()!!) {


                binding?.progressbar!!.setVisibility(View.GONE);
                mAdapter!!.removeLoadingFooter()
                isLoadingPaginationAdapterCallback = false
                isLoading = false

                mAdapter?.addAll(response.body()!!.responseContents)

                println("testing for two  = $currentPage--$TOTAL_PAGES")

                if (currentPage < TOTAL_PAGES!!) {
                    binding?.progressbar!!.setVisibility(View.VISIBLE);
                    mAdapter?.addLoadingFooter()
                    isLoading = true
                    isLastPage = false
                    println("testing for four  = $currentPage--$TOTAL_PAGES")
                } else {
                    isLastPage = true
                    binding?.progressbar!!.setVisibility(View.GONE);
//                    visitHistoryAdapter.removeLoadingFooter()
                    isLoading = false
                    isLastPage = true
                    println("testing for five  = $currentPage--$TOTAL_PAGES")
                }
            } else {
                println("testing for six  = $currentPage--$TOTAL_PAGES")
                binding?.progressbar!!.setVisibility(View.GONE);
                mAdapter?.removeLoadingFooter()
                isLoading = false
                isLastPage = true
            }
        }

        override fun onBadRequest(response: Response<TestProcessResponseModel>?) {
            isLoadingPaginationAdapterCallback = false
            binding?.progressbar!!.setVisibility(View.GONE);
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
            @SuppressLint("SetTextI18n")
            override fun onSuccessfulResponse(response: Response<LabTestSpinnerResponseModel>) {


                listfilteritem?.add(LabTestSpinnerresponseContent())
                listfilteritem?.addAll(response.body()?.responseContents!!)

                FilterTestNameResponseMap =
                    listfilteritem!!.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

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


            listfilteritemAssignSpinner?.add(LabAssignedToresponseContent())
            listfilteritemAssignSpinner?.addAll(responseBody!!.body()?.responseContents!!)

            FilterAssignSpinnereResponseMap =
                listfilteritemAssignSpinner!!.map { it?.uuid!! to it.name!! }!!.toMap()
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

    val sampleRecivedRetrofitCallback = object : RetrofitCallback<SimpleResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<SimpleResponseModel>?) {


            Toast.makeText(requireContext(), "Sample Received Successfully", Toast.LENGTH_LONG)
                .show()

            binding?.progressbar!!.setVisibility(View.GONE);

            mAdapter!!.clearAll()

            pageSize = 10

            currentPage = 0

            getTestProcessAPI(10, 0)


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

            binding?.progressbar!!.setVisibility(View.GONE);
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


    override fun onRefreshList() {

        Toast.makeText(requireContext(), "Rejected Successfully", Toast.LENGTH_LONG).show()

        mAdapter!!.clearAll()

        pageSize = 10

        currentPage = 0

        getTestProcessAPI(10, 0)
    }

    override fun onRefreshOrderList(from: String) {
        Toast.makeText(requireContext(), "Save Successfully", Toast.LENGTH_LONG).show()

        mAdapter!!.clearAll()

        pageSize = 10

        currentPage = 0

        if (from == "Process") {


            AnalyticsManager.getAnalyticsManager()
                .trackLMISLabProcessOrderApproval(requireContext(), "")

        }


        getTestProcessAPI(10, 0)
    }

    override fun onRefreshsendApprovalList() {
        mAdapter!!.clearAll()

        pageSize = 10

        currentPage = 0

        getTestProcessAPI(10, 0)

    }

    override fun onRefreshAssignToOrderList() {
        mAdapter!!.clearAll()

        pageSize = 10

        currentPage = 0

        getTestProcessAPI(10, 0)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is RejectDialogFragment) {
            childFragment.setOnLabTestRefreshListener(this)
        }
        if (childFragment is OrderProcessDialogFragment) {
            childFragment.setOnOrderProcessRefreshListener(this)
        }

        if (childFragment is SendForApprovalDialogFragment) {
            childFragment.setOnForApprovalRefreshListener(this)
        }

        if (childFragment is AssignToOtherDialogFragment) {
            childFragment.setOnAssignToOtherRefreshListener(this)
        }
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