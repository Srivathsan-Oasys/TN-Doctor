package com.oasys.digihealth.doctor.ui.order_status.ui

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
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.ActivityOrderStatusBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.order_status.model.*
import com.oasys.digihealth.doctor.ui.order_status.view_model.OrderStatusViewModel
import com.oasys.digihealth.doctor.ui.order_status.view_model.OrderStatusViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderStatusActivity : Fragment() {

    var binding: ActivityOrderStatusBinding? = null

    var utils: Utils? = null
    private var viewModel: OrderStatusViewModel? = null
    var appPreferences: AppPreferences? = null
    private var mAdapter: OrderStatusAdapter? = null
    var linearLayoutManager: LinearLayoutManager? = null
    private var currentPage = 0
    private var pageSize = 10
    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES: Int = 0
    private var quicksearch: String? = ""
    private var pin: String? = ""
    private var patientName: String? = ""
    private var isLoadingPaginationAdapterCallback: Boolean = false
    private var facility_id: Int = 0
    private var testNAmeAllData: ArrayList<TestNameResponseContent> = ArrayList()
    private var testNameSpinnerMap = mutableMapOf<Int, String>()
    private var testNameUuid: Int = 0
    private var ordStatusAllData: ArrayList<OrderStatusSpinnerresponseContent> = ArrayList()
    private var orderStatusSpinnerMap = mutableMapOf<Int, String>()
    private var orderStatusUuid: Int = 0

    //calender / Datepicker
    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null
    private var fromDate: String = ""
    private var toDate: String = ""
    private var fromDateRev: String = ""
    private var toDateRev: String = ""
    private var cal = Calendar.getInstance()
    private var startDate: String = ""
    private var endDate: String = ""

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.activity_order_status,
                container,
                false
            )
        viewModel = OrderStatusViewModelFactory(
            requireActivity().application
        )
            .create(OrderStatusViewModel::class.java)
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
        appPreferences = AppPreferences.getInstance(
            requireActivity().application,
            AppConstants.SHARE_PREFERENCE_NAME
        )

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        Log.i("", "" + facility_id)
        utils = Utils(requireContext())
        binding?.advanceSearchText?.setOnClickListener {

            if (binding?.advanceSearchLayout?.visibility == View.VISIBLE) {
                binding?.advanceSearchLayout?.visibility = View.GONE
            } else {
                binding?.advanceSearchLayout?.visibility = View.VISIBLE
            }
        }
        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding?.orderStatusRecyclerView!!.layoutManager = linearLayoutManager
        mAdapter = OrderStatusAdapter(requireContext())
        binding?.orderStatusRecyclerView!!.adapter = mAdapter
        viewModel?.getOrderStatus(
            facility_id, currentPage,
            pageSize, true, quicksearch!!, pin!!, patientName!!, "", "", "", "",
            OrderStatusRetrofitCallBack

        )
        binding?.searchButton?.setOnClickListener {

            if (!binding?.calendarEditText!!.text.trim().toString().isEmpty()) {

                startDate = fromDateRev + "T00:01:00.000Z"
                endDate = toDateRev + "T23:59:59.000Z"
            }

            quicksearch = binding?.qucikSearch?.text?.toString()
            pin = binding?.quickPin?.text?.toString()
            patientName = binding?.patientName?.text?.toString()
            mAdapter!!.clearAll()
            pageSize = 10
            currentPage = 0
            viewModel?.getOrderStatus(
                facility_id, currentPage,
                pageSize, false, quicksearch!!, pin!!, patientName!!, startDate, endDate,
                testNameUuid.toString(),
                orderStatusUuid.toString(),
                OrderStatusRetrofitCallBack
            )
            binding?.drawerLayout!!.closeDrawer(GravityCompat.END)

        }

        binding?.clear?.setOnClickListener {
            clearAllFields()

            initViews()
        }

        initViews()

        binding?.orderStatusRecyclerView?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (!isLoadingPaginationAdapterCallback) {
                        isLoadingPaginationAdapterCallback = true
                        currentPage += 1
                        if (currentPage <= TOTAL_PAGES) {
                            // Toast.makeText(requireContext(),""+currentPage,Toast.LENGTH_LONG).show()
                            viewModel?.getOrderStatusNextPage(
                                facility_id, currentPage,
                                pageSize, true, quicksearch!!, pin!!, patientName!!,
                                patientSearchNextRetrofitCallBack
                            )
                        }
                    }
                }
            }
        })


        //Mani Work
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

        viewModel?.getOrderStatusTestName(facility_id, testNameRetrofitCallback)

        spinnerSelection()

        //Mani work end

        return binding!!.root
    }

    private fun clearAllFields() {
        binding?.qucikSearch?.setText("")
        binding?.quickPin?.setText("")
        binding?.patientName?.setText("")
        binding?.testName?.setSelection(0)
        binding?.orderNumber?.setText("")
        binding?.orderStatus?.setSelection(0)
    }

    private fun initViews() {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        binding?.calendarEditText!!.setText("""${formatter.format(Date())}-${formatter.format(Date())}""")
        //startDate = utils!!.getAgedayDifferent(1)+ "T23:59:59.000Z"
        //endDate = sdf.format(Date())+"T23:59:59.000Z"
    }


    fun spinnerSelection() {
        binding?.testName!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    testNameUuid =
                        testNameSpinnerMap.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {

                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    testNameUuid =
                        testNameSpinnerMap.filterValues { it == itemValue }.keys.toList()[0]
                    //Log.e("sev",""+selectseverityUuid)
                }
            }

        binding?.orderStatus!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    orderStatusUuid =
                        orderStatusSpinnerMap.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {

                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    orderStatusUuid =
                        orderStatusSpinnerMap.filterValues { it == itemValue }.keys.toList()[0]
                    //Log.e("sev",""+selectseverityUuid)
                }
            }
    }


    val OrderStatusRetrofitCallBack = object : RetrofitCallback<OrderStatusResponseModel> {
        override fun onSuccessfulResponse(response: Response<OrderStatusResponseModel>) {
            binding?.completedCount?.text = "Completed Count " + response.body()?.completedCount!!
            binding?.pendingCount?.text = "Pending Count " + response.body()?.pendingCount!!
            if (response.body()?.responseContents?.isNotEmpty()!!) {
                TOTAL_PAGES =
                    Math.ceil(response.body()!!.totalRecords.toDouble() / 10).toInt()
                if (response.body()!!.responseContents.isNotEmpty()) {
                    binding?.progressbar!!.visibility = View.GONE
                    mAdapter!!.addAll(response.body()!!.responseContents)
                    if (currentPage < TOTAL_PAGES) {
                        isLoadingPaginationAdapterCallback = false
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

                if (response.body()!!.totalRecords < 11) {
                    binding?.progressbar!!.visibility = View.GONE
                }


            } else {
                Toast.makeText(context, "No records found", Toast.LENGTH_LONG).show()
                mAdapter!!.clearAll()
            }

        }


        override fun onBadRequest(response: Response<OrderStatusResponseModel>) {
            binding?.progressbar!!.visibility = View.GONE
            isLoadingPaginationAdapterCallback = false
            val gson = GsonBuilder().create()
            val responseModel: OrderStatusResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    OrderStatusResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.message
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
            isLoadingPaginationAdapterCallback = false
            viewModel!!.progress.value = 8
        }
    }

    val patientSearchNextRetrofitCallBack = object : RetrofitCallback<OrderStatusResponseModel> {
        override fun onSuccessfulResponse(response: Response<OrderStatusResponseModel>) {
            if (response.body()?.responseContents!!.isNotEmpty()) {

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

        override fun onBadRequest(response: Response<OrderStatusResponseModel>?) {
            binding?.progressbar!!.visibility = View.GONE
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


    val testNameRetrofitCallback = object : RetrofitCallback<TestNameResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<TestNameResponseModel>?) {

            testNAmeAllData.add(TestNameResponseContent())
            testNAmeAllData.addAll(responseBody?.body()?.responseContents as ArrayList<TestNameResponseContent>)
            testNameSpinnerMap =
                testNAmeAllData.map { it.uuid!! to it.name!! }.toMap().toMutableMap()
            val adapter =
                ArrayAdapter<String>(
                    context!!,
                    android.R.layout.simple_spinner_item,
                    testNameSpinnerMap.values.toMutableList()
                )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.testName!!.adapter = adapter

            viewModel?.getOrderStatusSpinner(facility_id, orderStatusRetrofitCallback)

        }

        override fun onBadRequest(errorBody: Response<TestNameResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: TestNameResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    TestNameResponseModel::class.java
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
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }

    }


    val orderStatusRetrofitCallback = object : RetrofitCallback<OrderStatusSpinnerResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<OrderStatusSpinnerResponseModel>?) {

            ordStatusAllData.add(OrderStatusSpinnerresponseContent())
            ordStatusAllData.addAll(responseBody?.body()?.responseContents as ArrayList<OrderStatusSpinnerresponseContent>)
            orderStatusSpinnerMap =
                ordStatusAllData.map { it.uuid!! to it.name!! }.toMap().toMutableMap()
            val adapter =
                ArrayAdapter<String>(
                    context!!,
                    android.R.layout.simple_spinner_item,
                    orderStatusSpinnerMap.values.toMutableList()
                )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.orderStatus!!.adapter = adapter

        }

        override fun onBadRequest(errorBody: Response<OrderStatusSpinnerResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: OrderStatusResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    OrderStatusResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.message
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
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }

    }
}