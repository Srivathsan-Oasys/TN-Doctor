package com.hmis_tn.doctor.ui.quick_reg.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.ActivityLabTestBinding
import com.hmis_tn.doctor.fire_base_analytics.AnalyticsManager
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.radiology.model.RecyclerDto
import com.hmis_tn.doctor.ui.home.HomeActivity
import com.hmis_tn.doctor.ui.quick_reg.model.ResponseTestMethod
import com.hmis_tn.doctor.ui.quick_reg.model.ResponseTestMethodContent
import com.hmis_tn.doctor.ui.quick_reg.model.SampleAcceptanceResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.Sample
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.SampleAcceptedRequest
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.*
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.orderRequest.OrderProcessDetail
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.orderRequest.OrderProcessDetailsResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.orderRequest.Req
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.LabAssignedToResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.LabAssignedToresponseContent
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.LabTestResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.SimpleResponseModel
import com.hmis_tn.doctor.ui.quick_reg.view_model.LabTestViewModel
import com.hmis_tn.doctor.ui.quick_reg.view_model.LabTestViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LabTestActivity : Fragment(), OrderProcessDialogFragment.OnOrderProcessListener,
    RejectDialogFragment.OnLabTestRefreshListener,
    SendForApprovalDialogFragment.OnsendForApprovalListener,
    AssignToOtherDialogFragment.OnAssignToOtherListener,
    OrderProcessDialogFragment.OnLabTestCallBack {

    private var selectTestitemUuid: String? = ""
    private var selectAssignitemUuid: String? = ""
    private var details: Detail? = Detail()
    var binding: ActivityLabTestBinding? = null
    var utils: Utils? = null
    private var viewModel: LabTestViewModel? = null
    private var isLoadingPaginationAdapterCallback: Boolean = false
    var appPreferences: AppPreferences? = null
    private val labTestList: MutableList<RecyclerDto> = ArrayList()
    private var mAdapter: LabTestAdapter? = null
    var linearLayoutManager: LinearLayoutManager? = null
    private var endDate: String = ""
    private var listfilteritem: ArrayList<ResponseTestMethodContent?>? = ArrayList()
    private var listfilteritemAssignSpinner: ArrayList<LabAssignedToresponseContent?>? = ArrayList()
    private var FilterTestNameResponseMap = mutableMapOf<Int, String>()
    private var FilterAssignSpinnereResponseMap = mutableMapOf<Int, String>()
    private var startDate: String = ""
    var checktestspinner = 0
    private var orderCount: Int = 0
    var checkassignedToSpinner = 0
    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null
    private var fromDate: String = ""
    private var toDate: String = ""
    var testMethodCode: String = ""

    private var pinOrMobile: String = ""
    private var orderNumber: String = ""

    private var orderId: ArrayList<SendIdList> = ArrayList()

    private var LabUUId: Int? = null

    var ACCEPTEDUUId: Int = 10

    var APPROVEDUUId: Int = 7

    var CREATEDUUID: Int = 1


    var EXECUTEDUUId: Int = 13

    var REJECTEDUUId: Int = 2

    var SAMPLE_RECEIVE: Int = 14
    var SAMPLE_IN_TRANSPORTUUId: Int = 16
    var SAMPLE_TRANSPORTUUId: Int = 15
    var SENDFORAPPROVALUUId: Int = 19

    var RETESTUUId: Int = 18

    private var fromDateRev: String = ""
    private var toDateRev: String = ""

    private var facility_id: Int = 0

    /////Pagination

    private var currentPage = 0
    private var pageSize = 10
    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES: Int = 0

    var cal = Calendar.getInstance()

    //private var customProgressDialog: CustomProgressDialog? = null
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.activity_lab_test,
                container,
                false
            )

        viewModel = LabTestViewModelFactory(
            requireActivity().application
        )
            .create(LabTestViewModel::class.java)
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
        binding?.labTestrecycleview!!.layoutManager = linearLayoutManager
        mAdapter = LabTestAdapter(requireContext(), ArrayList())
        binding?.labTestrecycleview!!.adapter = mAdapter

        appPreferences = AppPreferences.getInstance(
            requireActivity().application,
            AppConstants.SHARE_PREFERENCE_NAME
        )

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!

        LabUUId = appPreferences?.getInt(AppConstants.LAB_UUID)!!
        utils = Utils(requireContext())

        viewModel?.getTextMethod1(facility_id, getTestMethdCallBack1)

        binding?.labTestrecycleview?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

        /*      binding?.testSpinner!!.onItemSelectedListener =
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
      */        binding?.assignedToSpinner!!.onItemSelectedListener =
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

            Toast.makeText(context, "Select Start Date", Toast.LENGTH_LONG).show()
            val c: Calendar = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    Toast.makeText(context, "Select End Date", Toast.LENGTH_LONG).show()

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

        mAdapter!!.setOnPrintClickListener(object : LabTestAdapter.OnPrintClickListener {
            override fun onPrintClick(uuid: Int) {


                val bundle = Bundle()

                bundle.putInt("pdfid", uuid)

                bundle.putString("from", "test")

                val labtemplatedialog = LabPDF()

                labtemplatedialog.arguments = bundle

                (activity as HomeActivity).replaceFragment(labtemplatedialog)

            }
        })

        binding?.searchButton!!.setOnClickListener {

            if (!binding?.calendarEditText!!.text.trim().toString().isEmpty()) {

                startDate = fromDateRev + "T00:01:00.000Z"
                endDate = toDateRev + "T23:59:59.000Z"
            }

            pinOrMobile = binding?.searchUsingMobileNo!!.text.trim().toString()
            orderNumber = binding?.searchOrderNumber!!.text.trim().toString()

            binding?.drawerLayout!!.closeDrawer(GravityCompat.END)

            mAdapter!!.clearAll()

            pageSize = 10

            currentPage = 0

            labListAPI(pageSize, currentPage)
        }

        binding?.rejected?.setOnClickListener {

            val datas = mAdapter!!.getSelectedCheckData()

            var reqest: LabrapidSaveRequestModel = LabrapidSaveRequestModel()

            var detailsArray: ArrayList<SendIdList> = ArrayList()

            detailsArray.clear()

            var status: Boolean = true

            if (datas!!.size != 0) {


                for (i in datas.indices) {

                    if ((datas[i]!!.order_status_uuid == ACCEPTEDUUId || datas[i]!!.order_status_uuid == EXECUTEDUUId) || (datas[i]!!.order_status_uuid == SENDFORAPPROVALUUId || datas[i]!!.order_status_uuid == CREATEDUUID)) {

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

                    Toast.makeText(context, "Cannot process order", Toast.LENGTH_SHORT).show()

                }

            } else {

                Toast.makeText(context, "Please Select Any one Item", Toast.LENGTH_SHORT).show()

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

                var firstId = datas.get(0)!!.test_master_uuid

                for (i in datas.indices) {

                    if (datas[i]!!.test_method_uuid != 2 && (datas[i]!!.order_status_uuid == ACCEPTEDUUId || datas[i]!!.order_status_uuid == SAMPLE_TRANSPORTUUId)) {

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

                    Toast.makeText(context, "Cannot process order", Toast.LENGTH_SHORT).show()

                }

            } else {

                Toast.makeText(context, "Please Select Any one Item", Toast.LENGTH_SHORT).show()

            }

        }

        val sdf = SimpleDateFormat("yyyy-MM-dd")

        val formatter = SimpleDateFormat("dd-MM-yyyy")

        binding?.calendarEditText!!.setText("""${formatter.format(Date())}-${formatter.format(Date())}""")

        startDate = utils!!.getAgedayDifferent(1) + "T23:59:59.000Z"

        endDate = sdf.format(Date()) + "T23:59:59.000Z"

        labListAPI(pageSize, currentPage)

        binding?.clear!!.setOnClickListener {

            clearSearch()

        }

        binding?.sampleAcceptanceBtn!!.setOnClickListener {


            val request: SampleAcceptedRequest = SampleAcceptedRequest()

            val datas = mAdapter!!.getSelectedCheckData()

            var OrderList: ArrayList<Sample> = ArrayList()

            OrderList.clear()


            var status: Boolean = true

            if (datas!!.size != 0) {

                for (i in datas.indices) {

                    if (datas[i]!!.order_status_uuid == CREATEDUUID) {

                        var order: Sample = Sample()

                        order.Id = datas[i]!!.uuid!!


                        order.order_number = datas[i]!!.order_number!!

                        order.order_status_uuid = datas[i]!!.order_status_uuid!!

                        order.to_location_uuid = datas[i]!!.to_location_uuid!!

                        OrderList.add(order)

                    } else {

                        status = false

                    }
                }

                if (status) {

                    request.details = OrderList


                    viewModel!!.getLabSampleAcceptance(request, labTestAcceptRetrofitCallback)

                    //viewModel!!.orderDetailsGet(request, orderDetailsRetrofitCallback)

                } else {

                    Toast.makeText(context, "Already Sample Accepted", Toast.LENGTH_SHORT).show()

                }

            } else {

                Toast.makeText(context, "Please Select Any one Item", Toast.LENGTH_SHORT).show()

            }


        }

        binding!!.selectAllCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->

            mAdapter!!.selectAllCheckbox(isChecked)

        }

        binding!!.orderProccess!!.setOnClickListener {

            val request: Req = Req()

            val datas = mAdapter!!.getSelectedCheckData()

            var OrderList: ArrayList<OrderProcessDetail> = ArrayList()

            OrderList.clear()


            var idList: ArrayList<SendIdList> = ArrayList()


            var status: Boolean = true

            if (datas!!.size != 0) {

                orderCount = datas.size

                for (i in datas.indices) {

                    val testid = datas[0]!!.test_master_uuid

                    if (datas[i]!!.test_master_uuid == testid && ((datas[i]!!.order_status_uuid == ACCEPTEDUUId ||
                                datas[i]!!.order_status_uuid == EXECUTEDUUId) || datas[i]!!.order_status_uuid == RETESTUUId) && datas[i]!!.test_method_code != "Rapid Di"
                    ) {

                        var order: OrderProcessDetail = OrderProcessDetail()

                        order.Id = datas[i]!!.uuid!!

                        val reject: SendIdList = SendIdList()

                        reject.Id = datas[i]!!.uuid!!

                        idList.add(reject)

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
                        "Cannot be process Accepted and Executed Status only allowed",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            } else {

                Toast.makeText(context, "Please Select Any one Item", Toast.LENGTH_SHORT).show()

            }


        }

        binding!!.saveOfApproval.setOnClickListener {

            val datas = mAdapter!!.getSelectedCheckData()

            var reqest: LabrapidSaveRequestModel = LabrapidSaveRequestModel()

            var detailsArray: ArrayList<SendIdList> = ArrayList()

            detailsArray.clear()

            var status: Boolean = true

            if (datas!!.size != 0) {

                for (i in datas.indices) {

                    if (datas[i]!!.test_method_uuid != 2 && (datas[i]!!.order_status_uuid == EXECUTEDUUId || datas[i]!!.order_status_uuid == RETESTUUId)) {

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
                        "Cannot send for approval, Please check the status!",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            } else {

                Toast.makeText(context, "Please Select Any one Item", Toast.LENGTH_SHORT).show()

            }


        }

        binding!!.saveCardView.setOnClickListener {

            val datas = mAdapter!!.getSelectedCheckData()

            var reqest: LabrapidSaveRequestModel = LabrapidSaveRequestModel()

            var detailsArray: ArrayList<DetailX> = ArrayList()

            detailsArray.clear()

            var status: Boolean = true

            if (datas!!.size != 0) {

                for (i in datas.indices) {

                    if (datas[i]!!.test_method_uuid == 2) {
                        val details: DetailX = DetailX()

                        details.patient_order_test_detail_uuids = datas[i]!!.uuid!!

                        when (datas[i]!!.radioselectName) {
                            1 -> {

                                details.result_value = "Positive"

                                details.qualifier_uuid = 2

                            }
                            2 -> {

                                details.result_value = "Negative"

                                details.qualifier_uuid = 1


                            }
                            3 -> {

                                details.result_value = "Equivocal"

                                details.qualifier_uuid = 3

                            }
                            else -> {

                                status = false

                                Toast.makeText(context, "Select one Result", Toast.LENGTH_SHORT)
                                    .show()


                            }
                        }

                        detailsArray.add(details)

                    } else {

                        status = false

                    }
                }

                if (status) {


                    reqest.details = detailsArray

                    viewModel!!.rapidSave(reqest, saveRapidRetrofitCallback)

                } else {

                    Toast.makeText(
                        context,
                        "Cannot process order. please check test method",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            } else {

                Toast.makeText(context, "Please Select Any one Item", Toast.LENGTH_SHORT).show()

            }

        }


        binding?.autoCompleteTextView?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) {

                    selectTestitemUuid = ""
                }
            }
        })

        return binding!!.root
    }

    private fun labListSeacondAPI(
        pageSize: Int,
        currentPage: Int
    ) {
        val labTestRequestModel = LabTestRequestModel()
        labTestRequestModel.pageNo = currentPage
        labTestRequestModel.paginationSize = pageSize
        labTestRequestModel.search = ""
        labTestRequestModel.test_name = ""
        labTestRequestModel.to_facility_name = selectAssignitemUuid
        labTestRequestModel.order_number = orderNumber
        labTestRequestModel.test_method_name = selectTestitemUuid
        labTestRequestModel.is_lab_test = true
        labTestRequestModel.fromDate = startDate
        labTestRequestModel.toDate = endDate
        labTestRequestModel.to_facility_uuid = facility_id.toString()
        if (LabUUId != 0) {
            labTestRequestModel.to_location_uuid = LabUUId.toString()
            labTestRequestModel.lab_uuid = LabUUId.toString()
        }
        labTestRequestModel.pinOrMobile = pinOrMobile

        viewModel?.getLabListSecond(labTestRequestModel, labTestResponseSecondRetrofitCallback)
    }

    private fun labListAPI(
        pageSize: Int,
        currentPage: Int
    ) {
        //customProgressDialog!!.show()
        val labTestRequestModel = LabTestRequestModel()
        labTestRequestModel.pageNo = currentPage
        labTestRequestModel.paginationSize = pageSize
        labTestRequestModel.search = ""
        labTestRequestModel.test_name = ""
        labTestRequestModel.test_method_name = selectTestitemUuid
        labTestRequestModel.to_facility_name = selectAssignitemUuid
        labTestRequestModel.order_number = orderNumber
        labTestRequestModel.is_lab_test = true
        labTestRequestModel.fromDate = startDate
        labTestRequestModel.toDate = endDate
        labTestRequestModel.to_facility_uuid = facility_id.toString()

        if (LabUUId != 0) {
            labTestRequestModel.to_location_uuid = LabUUId.toString()
            labTestRequestModel.lab_uuid = LabUUId.toString()
        }
        labTestRequestModel.pinOrMobile = pinOrMobile

        viewModel?.getLabList(labTestRequestModel, labTestResponseRetrofitCallback)
    }

    val labTestResponseRetrofitCallback = object : RetrofitCallback<LabTestResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LabTestResponseModel>?) {
            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
                TOTAL_PAGES =
                    Math.ceil(responseBody.body()!!.totalRecords!!.toDouble() / 10).toInt()
                binding?.positiveTxt!!.text = "0"

                binding?.negativeTxt!!.text = "0"

                binding?.equivocalTxt!!.text = "0"

                binding?.rejectedTxt!!.text = "0"


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

                    if (diseaseList.size != 0 && diseaseList[i]?.result_value.equals("Positive")) {
                        binding?.positiveTxt!!.text = diseaseList[i]?.qualifier_count.toString()
                    } else if (diseaseList.size != 0 && diseaseList[i]?.result_value.equals("Negative")) {
                        binding?.negativeTxt!!.text = diseaseList[i]?.qualifier_count.toString()
                    } else if (diseaseList.size != 0 && diseaseList[i]?.result_value.equals("Equivocal")) {
                        binding?.equivocalTxt!!.text = diseaseList[i]?.qualifier_count.toString()
                    }

                }

                val orderList = responseBody.body()?.order_status_count

                if (orderList?.size != 0) {

                    for (i in orderList!!.indices) {

                        if (orderList[i]?.order_status_uuid == REJECTEDUUId) {

                            binding?.rejectedTxt!!.text = orderList[i]?.order_count.toString()

                        } else if (orderList[i]?.order_status_uuid == ACCEPTEDUUId) {

                            binding?.positiveTxt!!.text = orderList[i]?.order_count.toString()

                        } else if (orderList[i]?.order_status_uuid == SENDFORAPPROVALUUId) {

                            binding?.equivocalTxt!!.text = orderList[i]?.order_count.toString()

                        } else if (orderList[i]?.order_status_uuid == SAMPLE_TRANSPORTUUId) {

                            binding?.negativeTxt!!.text = orderList[i]?.order_count.toString()

                        }

                    }

                }


            } else {
                Toast.makeText(context, "No records found", Toast.LENGTH_LONG).show()
                binding?.progressbar!!.visibility = View.GONE
                binding?.positiveTxt!!.text = "0"

                binding?.negativeTxt!!.text = "0"

                binding?.equivocalTxt!!.text = "0"

                binding?.rejectedTxt!!.text = "0"

                mAdapter!!.clearAll()
            }

        }

        override fun onBadRequest(errorBody: Response<LabTestResponseModel>?) {
            isLoadingPaginationAdapterCallback = false
            val gson = GsonBuilder().create()
            val responseModel: LabTestResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    LabTestResponseModel::class.java
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
            binding?.progressbar!!.visibility = View.GONE

        }

    }

    val saveRapidRetrofitCallback = object : RetrofitCallback<SimpleResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<SimpleResponseModel>?) {

            AnalyticsManager.getAnalyticsManager().trackLMISLabSave(context!!, "")

            Toast.makeText(context, "Save Successfully", Toast.LENGTH_LONG).show()

            mAdapter!!.clearAll()

            pageSize = 10

            currentPage = 0

            labListAPI(10, 0)

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


    val orderDetailsRetrofitCallback = object : RetrofitCallback<OrderProcessDetailsResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<OrderProcessDetailsResponseModel>?) {


            val ft = childFragmentManager.beginTransaction()
            val dialog = OrderProcessDialogFragment()
            val bundle = Bundle()
            val saveArray = responseBody!!.body()!!.responseContents.rows
            bundle.putParcelableArrayList(AppConstants.RESPONSECONTENT, saveArray)
            bundle.putParcelableArrayList(AppConstants.RESPONSEORDERARRAY, orderId)
            bundle.putInt(AppConstants.RESPONSENEXT, orderCount)
            bundle.putString("testMethodCode", testMethodCode)
            bundle.putString("From", "Test")
            // send this arraylist orderId
            dialog.arguments = bundle

            // dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
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


    val labTestAcceptRetrofitCallback = object : RetrofitCallback<SampleAcceptanceResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<SampleAcceptanceResponseModel>?) {

            Toast.makeText(context, "Sample Accepted Successfully", Toast.LENGTH_SHORT).show()

            mAdapter!!.clearAll()

            pageSize = 10

            currentPage = 0

            labListAPI(10, 0)


        }

        override fun onBadRequest(errorBody: Response<SampleAcceptanceResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: SampleAcceptanceResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    SampleAcceptanceResponseModel::class.java
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


    val labTestResponseSecondRetrofitCallback = object : RetrofitCallback<LabTestResponseModel> {
        override fun onSuccessfulResponse(response: Response<LabTestResponseModel>) {
            if (response.body()?.responseContents!!.isNotEmpty()) {
                binding?.progressbar!!.visibility = View.GONE
                mAdapter!!.removeLoadingFooter()
                isLoading = false
                isLoadingPaginationAdapterCallback = false

                mAdapter?.addAll(response.body()!!.responseContents)

                println("testing for two  = $currentPage--$TOTAL_PAGES")

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
                binding?.progressbar!!.visibility = View.GONE
                println("testing for six  = $currentPage--$TOTAL_PAGES")
                mAdapter?.removeLoadingFooter()
                isLoading = false
                isLastPage = true
            }
        }

        override fun onBadRequest(response: Response<LabTestResponseModel>?) {
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
            binding?.progressbar!!.visibility = View.GONE
        }
    }

    val getTestMethdCallBack1 =
        object : RetrofitCallback<ResponseTestMethod> {
            @SuppressLint("SetTextI18n")
            override fun onSuccessfulResponse(response: Response<ResponseTestMethod>) {

                //      listfilteritem?.add(ResponseTestMethodContent())
                listfilteritem?.addAll((response.body()?.responseContents)!!)

                setMethod(response.body()?.responseContents)


/*
                FilterTestNameResponseMap =
                    listfilteritem!!.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
                try {
                    val adapter =
                        ArrayAdapter<String>(
                            context!!,
                            android.R.layout.simple_spinner_item,
                            FilterTestNameResponseMap.values.toMutableList()
                        )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding?.testSpinner!!.adapter = adapter
                } catch (e: Exception) {

                }
                binding?.testSpinner?.prompt = listfilteritem?.get(0)?.name
                binding?.testSpinner?.setSelection(0)
*/

                viewModel?.getTextAssignedTo(facility_id, LabAssignedSpinnerRetrofitCallback)
            }

            override fun onBadRequest(response: Response<ResponseTestMethod>) {
                val gson = GsonBuilder().create()
                val responseModel: ResponseTestMethod
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        ResponseTestMethod::class.java
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

    private fun setMethod(responseContents: List<ResponseTestMethodContent?>?) {

        val adap = LabTestdropdownAdapter(
            this.requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents
        )

        binding?.autoCompleteTextView?.threshold = 1
        binding?.autoCompleteTextView?.setAdapter(adap)


        binding?.autoCompleteTextView?.setOnItemClickListener { parent, _, pos, id ->
            val selectedPoi = parent.adapter.getItem(pos) as ResponseTestMethodContent?

            binding?.autoCompleteTextView?.setText(selectedPoi?.name)

            selectTestitemUuid = selectedPoi?.uuid.toString()


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
                        context!!,
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

    private fun clearSearch() {

        binding?.searchUsingMobileNo!!.setText("")
        binding?.searchOrderNumber!!.setText("")
        binding?.assignedToSpinner?.setSelection(0)

        binding?.autoCompleteTextView?.setText("")
        selectTestitemUuid = ""
        // binding?.testSpinner?.setSelection(0)

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

        if (childFragment is OrderProcessDialogFragment) {
            childFragment.setOnLabTestProcess(this)
        }
    }

    override fun onRefreshList() {
        //RejectDialogFragment
        Toast.makeText(requireContext(), "Rejected Successfully", Toast.LENGTH_LONG).show()

        mAdapter!!.clearAll()

        pageSize = 10

        currentPage = 0

        labListAPI(10, 0)

    }

    override fun onRefreshOrderList(from: String) {

        Toast.makeText(requireContext(), "Save Successfully", Toast.LENGTH_LONG).show()

        mAdapter!!.clearAll()

        pageSize = 10

        currentPage = 0

        if (from == "Test") {


            AnalyticsManager.getAnalyticsManager()
                .trackLMISLabTestOrderApproval(requireContext(), "")

        }

        labListAPI(10, 0)
    }

    override fun onRefreshsendApprovalList() {
        mAdapter!!.clearAll()

        pageSize = 10

        currentPage = 0



        labListAPI(10, 0)
    }

    override fun onRefreshAssignToOrderList() {
        mAdapter!!.clearAll()

        pageSize = 10

        currentPage = 0

        labListAPI(10, 0)
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