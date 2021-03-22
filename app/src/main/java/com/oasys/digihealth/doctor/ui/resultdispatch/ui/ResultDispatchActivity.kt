package com.oasys.digihealth.doctor.ui.resultdispatch.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.ActivityResultDispatchBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.resultdispatch.model.ResponseContentsResultDispatch
import com.oasys.digihealth.doctor.ui.resultdispatch.model.ResponseResultDispatch
import com.oasys.digihealth.doctor.ui.resultdispatch.request.RequestDispatchSearch
import com.oasys.digihealth.doctor.ui.resultdispatch.viewmodel.ResultDispatchViewModel
import com.oasys.digihealth.doctor.ui.resultdispatch.viewmodel.ResultDispatchViewModelFactory
import com.oasys.digihealth.doctor.ui.sample_dispatch.ui.ResultDispatchAdapter
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ResultDispatchActivity : Fragment() {

    var binding: ActivityResultDispatchBinding? = null
    var utils: Utils? = null
    private var viewModel: ResultDispatchViewModel? = null
    var appPreferences: AppPreferences? = null

    private var mAdapter: ResultDispatchAdapter? = null
    var linearLayoutManager: LinearLayoutManager? = null
    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null
    private var fromDate: String = ""
    private var toDate: String = ""
    private var fromDateRev: String = ""
    private var toDateRev: String = ""
    private var isLoadingPaginationAdapterCallback: Boolean = false
    var cal = Calendar.getInstance()

    private var startDate: String = ""
    private var endDate: String = ""
    /////Pagination

    private var currentPage = 0
    private var pageSize = 10
    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES: Int = 0
    private var facility_id: Int = 0

    private var Labuuid: Int = 0
    private var pinnumber: String = ""
    private var searchUsingOrderNo: String = ""
    private var qucik_search: String = ""


    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.activity_result_dispatch,
                container,
                false
            )
        viewModel = ResultDispatchViewModelFactory(
            requireActivity().application
        )
            .create(ResultDispatchViewModel::class.java)
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
        binding?.sampleDispatchrecycleview!!.layoutManager = linearLayoutManager
        mAdapter = ResultDispatchAdapter(requireContext(), ArrayList())
        binding?.sampleDispatchrecycleview!!.adapter = mAdapter
        appPreferences = AppPreferences.getInstance(
            requireActivity().application,
            AppConstants.SHARE_PREFERENCE_NAME
        )

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        utils = Utils(requireContext())
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        Labuuid = appPreferences?.getInt(AppConstants.LAB_UUID)!!
        utils = Utils(requireContext())

        qucik_search = ""
        pinnumber = ""
        searchUsingOrderNo = ""

        labResultDispatchListAPI(pageSize, currentPage)

        initViews()
        listeners()

        return binding!!.root
    }

    private fun initViews() {
        val sdf = SimpleDateFormat("yyyy-MM-dd")

        val formatter = SimpleDateFormat("dd-MM-yyyy")

        binding?.calendarEditText!!.setText("""${formatter.format(Date())}-${formatter.format(Date())}""")

        //startDate = utils!!.getAgedayDifferent(1)+ "T23:59:59.000Z"

        //endDate = sdf.format(Date())+"T23:59:59.000Z"
    }

    private fun listeners() {
        binding?.advanceSearchText?.setOnClickListener {

            if (binding?.advanceSearchLayout?.visibility == View.VISIBLE) {
                binding?.advanceSearchLayout?.visibility = View.GONE


            } else {
                binding?.advanceSearchLayout?.visibility = View.VISIBLE


            }
        }

        binding?.searchButton!!.setOnClickListener {

            if (!binding?.calendarEditText!!.text.trim().toString().isEmpty()) {

                startDate = fromDateRev + "T00:01:00.000Z"
                endDate = toDateRev + "T23:59:59.000Z"
            }

            qucik_search = binding?.qucikSearch!!.text.trim().toString()
            pinnumber = binding?.patientDetailsPin!!.text.trim().toString()
            searchUsingOrderNo = binding?.searchUsingOrderNo!!.text.trim().toString()

            binding?.drawerLayout!!.closeDrawer(GravityCompat.END)

            mAdapter!!.clearAll()

            pageSize = 10

            currentPage = 0

            labResultDispatchListAPI(pageSize, currentPage)
        }

        mAdapter!!.setOnItemClickListener(object :
            ResultDispatchAdapter.OnItemClickListener {


            override fun onItemClick(
                responseContent: ResponseContentsResultDispatch?,
                position: Int
            ) {
                val bundle = Bundle()
                bundle.putInt(
                    "pdfid",
                    responseContent?.patient_order_detail?.patient_order_test_details_uuid!!
                )
                val labTechnicianFragment = ResultPDFViewerActivity()
                labTechnicianFragment.arguments = bundle//passing data to fragment
                val fragmentManager: FragmentManager? = fragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
                fragmentTransaction.replace(R.id.landfragment, labTechnicianFragment)
                fragmentTransaction.commit()
            }
        })

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
                            resultDispatchSeacondAPI(pageSize, currentPage)
                        }

                    }
                }
            }
        })

        binding?.clear?.setOnClickListener {
            clearAllFields()

            initViews()
        }
    }

    private fun clearAllFields() {
        binding?.qucikSearch?.setText("")
        binding?.patientDetailsPin?.setText("")
        binding?.searchUsingOrderNo?.setText("")
        binding?.calendarEditText?.setText("")
    }

    private fun resultDispatchSeacondAPI(
        pageSize: Int,
        currentPage: Int
    ) {

        //customProgressDialog!!.show()
        val requestResultdiapatch = RequestDispatchSearch()
        requestResultdiapatch.pageNo = currentPage
        requestResultdiapatch.paginationSize = pageSize
        requestResultdiapatch.search = ""
        requestResultdiapatch.sortField = "modified_date"
        requestResultdiapatch.sortOrder = "DESC"
        requestResultdiapatch.search = qucik_search
        requestResultdiapatch.order_number = searchUsingOrderNo
        requestResultdiapatch.uhid = pinnumber
        requestResultdiapatch.fromDate = startDate
        requestResultdiapatch.toDate = endDate
        viewModel?.getresultdispatchsecond(
            requestResultdiapatch,
            secondresultdispatchResponseRetrofitCallback
        )


    }

    private fun labResultDispatchListAPI(
        pageSize: Int,
        currentPage: Int
    ) {
        //customProgressDialog!!.show()
        val requestResultdiapatch = RequestDispatchSearch()
        requestResultdiapatch.pageNo = currentPage
        requestResultdiapatch.paginationSize = pageSize
        requestResultdiapatch.search = ""
        requestResultdiapatch.sortField = "modified_date"
        requestResultdiapatch.sortOrder = "DESC"
        requestResultdiapatch.search = qucik_search
        requestResultdiapatch.order_number = searchUsingOrderNo
        requestResultdiapatch.uhid = pinnumber
        requestResultdiapatch.fromDate = startDate
        requestResultdiapatch.toDate = endDate

        viewModel?.getresultdispatch(requestResultdiapatch, resultdispatchResponseRetrofitCallback)
    }

    val resultdispatchResponseRetrofitCallback = object : RetrofitCallback<ResponseResultDispatch> {
        override fun onSuccessfulResponse(responseBody: Response<ResponseResultDispatch>?) {
            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
                binding?.resultDispatchCount?.text =
                    "Result Dispatch Count " + responseBody.body()?.totalRecords
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

            } else {

                binding?.resultDispatchCount?.text = "Result Dispatch Count 0 "
                Toast.makeText(context, "No records found", Toast.LENGTH_LONG).show()
            }
        }

        override fun onBadRequest(errorBody: Response<ResponseResultDispatch>?) {
            isLoadingPaginationAdapterCallback = false
            val gson = GsonBuilder().create()
            val responseModel: ResponseResultDispatch
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    ResponseResultDispatch::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.msg!!
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
            isLoadingPaginationAdapterCallback = false
            binding?.progressbar!!.visibility = View.GONE
        }
    }

    val secondresultdispatchResponseRetrofitCallback =
        object : RetrofitCallback<ResponseResultDispatch> {
            override fun onSuccessfulResponse(response: Response<ResponseResultDispatch>) {
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

            override fun onBadRequest(response: Response<ResponseResultDispatch>?) {
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
}