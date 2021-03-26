package com.hmis_tn.doctor.ui.sample_dispatch.ui

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
import com.hmis_tn.doctor.databinding.ActivitySampleDispatchBinding
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.radiology.model.RecyclerDto
import com.hmis_tn.doctor.ui.home.HomeActivity
import com.hmis_tn.doctor.ui.quick_reg.model.ResponseTestMethod
import com.hmis_tn.doctor.ui.quick_reg.model.ResponseTestMethodContent
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.LabrapidSaveRequestModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.SendIdList
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.LabAssignedToResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.LabAssignedToresponseContent
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.LabTestApprovalResponseModel
import com.hmis_tn.doctor.ui.quick_reg.view.RejectDialogFragment
import com.hmis_tn.doctor.ui.sample_dispatch.model.SampleDispatchRequest
import com.hmis_tn.doctor.ui.sample_dispatch.view_model.SampleDispatchViewModel
import com.hmis_tn.doctor.ui.sample_dispatch.view_model.SampleDispatchViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SampleDispatchActivity : Fragment(), RejectDialogFragment.OnLabTestRefreshListener {

    var binding: ActivitySampleDispatchBinding? = null
    var utils: Utils? = null
    private var viewModel: SampleDispatchViewModel? = null
    var appPreferences: AppPreferences? = null
    private val sampleDispatchList: ArrayList<RecyclerDto> = ArrayList()
    private var mAdapter: SampleDispatchAdapter? = null
    var linearLayoutManager: LinearLayoutManager? = null
    private var listfilteritemAssignSpinner: ArrayList<LabAssignedToresponseContent?>? = ArrayList()
    private var FilterTestNameResponseMap = mutableMapOf<Int, String>()
    private var FilterAssignSpinnereResponseMap = mutableMapOf<Int, String>()
    var selectFacilityUUid: String = ""
    private var listfilteritem: ArrayList<ResponseTestMethodContent?>? = ArrayList()
    private var endDate: String = ""
    private var startDate: String = ""

    private var selectTestitemUuid: String? = ""
    private var selectAssignitemUuid: String? = ""
    /////Pagination

    private var currentPage = 0
    private var pageSize = 10
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
    private var isLoadingPaginationAdapterCallback: Boolean = false
    var cal = Calendar.getInstance()

    var ACCEPTEDUUId: Int = 10

    var APPROVEDUUId: Int = 7

    var EXECUTEDUUId: Int = 13
    var APPROVELPENDINGAUTH: Int = 1

    var REJECTEDUUId: Int = 2

    var SAMPLE_RECEIVE: Int = 14
    var SAMPLE_IN_TRANSPORTUUId: Int = 16

    var SAMPLE_TRANSPORTUUId: Int = 15
    var SENDFORAPPROVALUUId: Int = 19

    private var facility_id: Int = 0

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.activity_sample_dispatch,
                container,
                false
            )
        viewModel = SampleDispatchViewModelFactory(
            requireActivity().application
        ).create(SampleDispatchViewModel::class.java)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        utils = Utils(requireContext())

        viewModel?.getTextMethod1(facility_id, getTestMethdCallBack1)

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
        // prepareMovieData()
        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding?.sampleDispatchrecycleview!!.layoutManager = linearLayoutManager

        mAdapter = SampleDispatchAdapter(requireContext(), ArrayList())
        binding?.sampleDispatchrecycleview!!.adapter = mAdapter
        appPreferences = AppPreferences.getInstance(
            requireActivity().application,
            AppConstants.SHARE_PREFERENCE_NAME
        )

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!

        LabUUId = appPreferences?.getInt(AppConstants.LAB_UUID)!!
        utils = Utils(requireContext())

        binding!!.print.setOnClickListener {

            val datas = mAdapter!!.getSelectedCheckData()

            var reqest = LabrapidSaveRequestModel()

            val detailsArray: ArrayList<SendIdList> = ArrayList()

            detailsArray.clear()

            var status = true

            if (datas!!.size != 0) {


                val bundle = Bundle()

                bundle.putInt("pdfid", 26)

                val labtemplatedialog = DispatchPDF()

                labtemplatedialog.arguments = bundle

                (activity as HomeActivity).replaceFragment(labtemplatedialog)

/*                for (i in datas.indices) {

                    if (datas[i]!!.order_status_uuid == SAMPLE_TRANSPORTUUId) {

                        val details: SendIdList = SendIdList()

                        details.Id = datas[i]!!.uuid!!

                        detailsArray.add(details)

                    } else {

                        status = false

                    }
                }

                if (status) {




                } else {

                    Toast.makeText(context, "Cannot process order", Toast.LENGTH_SHORT).show()

                }*/

            } else {

                Toast.makeText(context, "Please Select Any one Item", Toast.LENGTH_SHORT).show()

            }


        }

        binding?.clearCardView?.setOnClickListener {
            binding?.testSpinner?.prompt = listfilteritem?.get(0)?.name
            binding?.testSpinner?.setSelection(0)

            binding?.assignedToSpinner?.prompt = listfilteritem?.get(0)?.name
            binding?.assignedToSpinner?.setSelection(0)

            binding?.searchUsingMobileNo?.setText("")
            binding?.searchOrderNumber?.setText("")

        }




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

        binding!!.selectAllCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->

            mAdapter!!.selectAllCheckbox(isChecked)

        }
/*
        binding?.rejected?.setOnClickListener{
            val ft = childFragmentManager.beginTransaction()
            val dialog = SampleDispatchRejectDialogFragment()

            dialog.show(ft, "Tag")


        }*/
        binding?.rejected?.setOnClickListener {

            val datas = mAdapter!!.getSelectedCheckData()

            var reqest = LabrapidSaveRequestModel()

            val detailsArray: ArrayList<SendIdList> = ArrayList()

            detailsArray.clear()

            var status = true

            if (datas!!.size != 0) {

                for (i in datas.indices) {

                    if (datas[i]!!.order_status_uuid == SAMPLE_TRANSPORTUUId) {

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
        binding?.disaptch?.setOnClickListener {

            val datas = mAdapter!!.getSelectedCheckData()

            val detailsArray: ArrayList<Int> = ArrayList()

            val dispatchId: ArrayList<Int> = ArrayList()

            detailsArray.clear()

            var status = true

            if (datas!!.size != 0) {

                val stage = datas[0]!!.test_method_uuid!!

                for (i in datas.indices) {

                    if (datas[i]!!.order_status_uuid == SAMPLE_TRANSPORTUUId && datas[i]!!.test_method_uuid == stage) {

                        detailsArray.add(datas[i]!!.sample_transport_details_uuid!!)

                        // dispatchId.add(datas[i]!!.dispatch_uuid!!)

                    } else {

                        status = false

                    }
                }

                if (status) {

//                    val datas:IntArray=detailsArray as IntArray

                    val ft = childFragmentManager.beginTransaction()
                    val dialog = DispatchDialogFragment()
                    val bundle = Bundle()

                    bundle.putIntegerArrayList(AppConstants.RESPONSECONTENT, detailsArray)

                    //     bundle.putIntegerArrayList(AppConstants.RESPONSEDISPATCH, dispatchId)

                    bundle.putInt(AppConstants.RESPONSENEXT, datas[0]!!.to_facility_uuid!!)

                    dialog.arguments = bundle

                    dialog.show(ft, "Tag")


                } else {

                    Toast.makeText(
                        context,
                        "Cannot process order Its's Already dispatched",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            } else {

                Toast.makeText(context, "Please Select Any one Item", Toast.LENGTH_SHORT).show()

            }


        }

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        binding?.calendarEditText!!.setText("""${formatter.format(Date())}-${formatter.format(Date())}""")
        startDate = utils!!.getAgedayDifferent(1) + "T18:30:00.000Z"
        endDate = sdf.format(Date()) + "T18:29:59.000Z"
        SampleDispathListAPI(pageSize, currentPage)


        binding?.sampleDispatchrecycleview?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (!isLoadingPaginationAdapterCallback) {
                        isLoadingPaginationAdapterCallback = true
                        currentPage += 1
                        if (currentPage <= TOTAL_PAGES) {
                            // Toast.makeText(requireContext(),""+currentPage,Toast.LENGTH_LONG).show()
                            SampleDispathSecondListAPI(pageSize, currentPage)
                        }

                    }
                }
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

            SampleDispathListAPI(pageSize, currentPage)
        }



        return binding!!.root
    }


    private fun SampleDispathListAPI(pageSize: Int, currentPage: Int) {

        val labTestApprovalRequestModel = SampleDispatchRequest()

        labTestApprovalRequestModel.pageNo = currentPage
        labTestApprovalRequestModel.paginationSize = pageSize
        labTestApprovalRequestModel.search = ""
        labTestApprovalRequestModel.to_facility_name = selectFacilityUUid
        labTestApprovalRequestModel.order_number = orderNumber
        labTestApprovalRequestModel.fromDate = startDate
        labTestApprovalRequestModel.toDate = endDate
        val arrayList: ArrayList<Int> = ArrayList()
        arrayList.add(14)
        arrayList.add(15)
        arrayList.add(16)
        labTestApprovalRequestModel.order_status_uuid = arrayList
        labTestApprovalRequestModel.pinOrMobile = pinOrMobile
        labTestApprovalRequestModel.widget_filter = ""
        labTestApprovalRequestModel.qualifier_filter = ""
        labTestApprovalRequestModel.test_method_name = selectTestitemUuid!!

        if (LabUUId != 0) {
            labTestApprovalRequestModel.lab_uuid = LabUUId.toString()

            labTestApprovalRequestModel.to_location_uuid = LabUUId.toString()

        }

        val labtest1 = Gson().toJson(labTestApprovalRequestModel)

        Log.i("", "" + labtest1)
        Log.i("", "" + labtest1)

        viewModel!!.getLabTestApprovalList(
            labTestApprovalRequestModel,
            labTestApprovalResponseRetrofitCallback
        )

    }

    private fun SampleDispathSecondListAPI(pageSize: Int, currentPage: Int) {

        val labTestApprovalRequestModel = SampleDispatchRequest()

        labTestApprovalRequestModel.pageNo = currentPage
        labTestApprovalRequestModel.paginationSize = pageSize
        labTestApprovalRequestModel.search = ""
        labTestApprovalRequestModel.test_method_name = selectTestitemUuid!!
        labTestApprovalRequestModel.to_facility_name = selectFacilityUUid
        labTestApprovalRequestModel.order_number = orderNumber
        labTestApprovalRequestModel.fromDate = startDate
        labTestApprovalRequestModel.toDate = endDate
        val arrayList: ArrayList<Int> = ArrayList()
        arrayList.add(14)
        arrayList.add(15)
        arrayList.add(16)
        labTestApprovalRequestModel.order_status_uuid = arrayList
        labTestApprovalRequestModel.pinOrMobile = pinOrMobile
        labTestApprovalRequestModel.widget_filter = ""
        labTestApprovalRequestModel.qualifier_filter = ""
        if (LabUUId != 0) {
            labTestApprovalRequestModel.lab_uuid = LabUUId.toString()

            labTestApprovalRequestModel.to_location_uuid = LabUUId.toString()

        }


        val labtest1 = Gson().toJson(labTestApprovalRequestModel)
        Log.i("", "" + labtest1)
        viewModel!!.getLabTestSecondApprovalList(
            labTestApprovalRequestModel,
            labTestApprovalSecondResponseRetrofitCallback
        )

    }

    val labTestApprovalResponseRetrofitCallback =
        object : RetrofitCallback<LabTestApprovalResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<LabTestApprovalResponseModel>?) {
                Log.e("labTestResponse", responseBody?.body()?.responseContents.toString())

                val responsedata = Gson().toJson(responseBody?.body()?.responseContents)

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

                    val diseaseList = responseBody.body()?.order_status_count
                    for (i in diseaseList!!.indices) {

                        if (diseaseList.size != 0 && diseaseList[i]?.order_status_uuid == SAMPLE_TRANSPORTUUId) {
                            binding?.positiveTxt!!.text = diseaseList[i]?.order_count.toString()
                        } else if (diseaseList.size != 0 && diseaseList[i]?.order_status_uuid == SAMPLE_RECEIVE) {
                            binding?.negativeTxt!!.text = diseaseList[i]?.order_count.toString()
                        } else if (diseaseList.size != 0 && diseaseList[i]?.order_status_uuid == SAMPLE_IN_TRANSPORTUUId) {
                            binding?.equivocalTxt!!.text = diseaseList[i]?.order_count.toString()
                        } else if (diseaseList.size != 0 && diseaseList[i]?.order_status_uuid == REJECTEDUUId) {

                            binding?.rejectedTxt!!.text = diseaseList[i]?.order_count.toString()

                        }
                    }

                    val orderList = responseBody.body()?.order_status_count

                    if (orderList?.size != 0) {

                        for (i in orderList!!.indices) {

                            if (orderList[i]?.order_status_uuid == 2) {

                                binding?.rejectedTxt!!.text = orderList[i]?.order_count.toString()

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


    val labTestApprovalSecondResponseRetrofitCallback =
        object : RetrofitCallback<LabTestApprovalResponseModel> {
            override fun onSuccessfulResponse(response: Response<LabTestApprovalResponseModel>) {
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
                binding?.progressbar!!.visibility = View.GONE
            }
        }


    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is RejectDialogFragment) {
            childFragment.setOnLabTestRefreshListener(this)
        }

    }

    override fun onRefreshList() {

        mAdapter!!.clearAll()

        pageSize = 10
        currentPage = 0

        SampleDispathListAPI(pageSize, currentPage)

    }

    val getTestMethdCallBack1 =
        object : RetrofitCallback<ResponseTestMethod> {
            @SuppressLint("SetTextI18n")
            override fun onSuccessfulResponse(response: Response<ResponseTestMethod>) {
                Log.i("", "" + response.body()?.responseContents)
                Log.i("", "" + response.body()?.req)
                listfilteritem?.add(ResponseTestMethodContent())
                listfilteritem?.addAll((response.body()?.responseContents)!!)
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
}