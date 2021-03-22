package com.oasys.digihealth.doctor.ui.out_patient.view

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.callbacks.FragmentBackClick
import com.oasys.digihealth.doctor.component.extention.hide
import com.oasys.digihealth.doctor.component.extention.isvisible
import com.oasys.digihealth.doctor.component.extention.show
import com.oasys.digihealth.doctor.component.extention.slideDown
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentOutPatientListingBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.view.EmrWorkFlowActivity
import com.oasys.digihealth.doctor.ui.home.HomeActivity
import com.oasys.digihealth.doctor.ui.out_patient.search_response_model.OldPatientResponseModule
import com.oasys.digihealth.doctor.ui.out_patient.search_response_model.ResponseContent
import com.oasys.digihealth.doctor.ui.out_patient.search_response_model.SearchResponseModel
import com.oasys.digihealth.doctor.ui.out_patient.view_model.OutPatientNewViewModel
import com.oasys.digihealth.doctor.ui.out_patient.view_model.OutPatientNewViewModelFactorty
import com.oasys.digihealth.doctor.ui.scanner.ScannerActivity
import com.oasys.digihealth.doctor.utils.Utils
import com.oasys.digihealth.doctor.utils.custom_views.CustomProgressDialog
import kotlinx.android.synthetic.main.dialog_add_casualty_patient.*
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class OutPatientFragmentNew : Fragment(), OldPatientSaveDialogFragment.OnOldPatientProcessListener {


    private var binding: FragmentOutPatientListingBinding? = null
    private var viewModel: OutPatientNewViewModel? = null
    private var utils: Utils? = null
    private var fragmentBackClick: FragmentBackClick? = null
    private var appPreferences: AppPreferences? = null
    private var facility_UUID: Int? = 0

    private var currentPage = 0
    private var pageSize = 10
    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES: Int = 0

    private var selectedDate: String = ""

    lateinit var outPatientAdapter: OutPatientAdapter
    private var isLoadingPaginationAdapterCallback: Boolean = false
    private var queryvalue: String? = ""
    private var departmentSearch: Boolean? = true

    private var startYear = 0
    private var startMonthOfYear: Int = 0
    private var startDayOfMonth: Int = 0
    val startDate = Calendar.getInstance()
    var customProgressDialog: CustomProgressDialog? = null


    override fun onRefreshOldPatientList(responseContent: ResponseContent) {
        TODO("Not yet implemented")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_out_patient_listing,
                container,
                false
            )
        initDropDown()
        initPerference()
        initDatePicker()
        initVisitOutPatientAdapter()
        callIntialSearch()

        binding?.qrCodeImageview?.setOnClickListener {
            val intent = Intent(requireActivity(), ScannerActivity::class.java)
            startActivityForResult(intent, 122)
        }

        binding?.etSearch?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length < 10) {
                    binding?.etSearch?.error = "Please enter minimum of 10 nos"

                }
            }
        })

        binding?.etSearch?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding?.etSearch?.text.toString().trim().length >= 10) {
                    showCustomProgressDialog()
                    currentPage = 0
                    queryvalue = binding?.etSearch?.text.toString()
                    outPatientAdapter.clearAll()
                    departmentSearch = false
                    viewModel?.searchPatient(
                        "",
                        "",
                        queryvalue!!,
                        binding?.etDischargeSummaryDate?.text.toString(),
                        currentPage,
                        pageSize,
                        "modified_date",
                        "DESC",
                        patientSearchRetrofitCallBack
                    )
                }
            }
            true
        }

        binding?.searchButton?.setOnClickListener {
            try {
                val view: View = requireActivity().currentFocus!!
                if (view != null) {
                    val imm: InputMethodManager =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            } catch (e: Exception) {

            }

            val pinnumber = binding?.etExsistingPin?.text.toString()
            val searchview = binding?.etSearch?.text.toString()
            if (pinnumber.isNotEmpty()) {
                outPatientAdapter.clearAll()
                viewModel?.getOldPatient(
                    pinnumber,
                    facility_UUID,
                    oldSEarchResponseCallback
                )
                return@setOnClickListener
            } else if (searchview.isNotEmpty() && searchview.length >= 10) {
                showCustomProgressDialog()
                currentPage = 0
                queryvalue = searchview
                outPatientAdapter.clearAll()
                departmentSearch = false
                viewModel?.searchPatient(
                    "",
                    "",
                    searchview,
                    binding?.etDischargeSummaryDate?.text.toString(),
                    currentPage,
                    pageSize,
                    "modified_date",
                    "DESC",
                    patientSearchRetrofitCallBack
                )
                return@setOnClickListener
            } else {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Please fill any one field"
                )
            }
        }

        binding?.clearcardview?.setOnClickListener {
            binding?.etSearch?.setText("")
            binding?.etExsistingPin?.setText("")
            binding?.etUfcPin?.setText("")
            binding?.etDischargeSummaryDate?.setText(getCurrentDate("dd-MMM-yyyy"))
            callIntialSearch()
            hideDropDown()
        }
        return binding!!.root
    }

    fun initDropDown() {
        binding?.llDropDownView?.hide()
        binding?.rlHeader?.setOnClickListener {
            if (binding?.llDropDownView?.isvisible()!!) {
                hideDropDown()
            } else {
                showDropDown()
            }
        }
    }

    fun showDropDown() {
        slideDown(requireContext(), binding?.llDropDownView!!)
        binding?.llDropDownView?.show()
        binding?.ivArrow?.rotation = 270F
    }

    fun hideDropDown() {
        slideDown(requireContext(), binding?.llDropDownView!!)
        binding?.ivArrow?.rotation = 90F
        binding?.llDropDownView?.hide()
    }

    fun initPerference() {
        if (activity !is FragmentBackClick) {
//            throw ClassCastException("Hosting activity must implement BackHandlerInterface")
        } else {
            fragmentBackClick = activity as FragmentBackClick?
        }
        viewModel = OutPatientNewViewModelFactorty(
            requireActivity().application
        )
            .create(OutPatientNewViewModel::class.java)
        customProgressDialog = CustomProgressDialog(requireContext())
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        utils = Utils(requireContext())
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
    }

    private fun initDatePicker() {
        val sdf = SimpleDateFormat("dd-mm-yyy HH:mm:ss", Locale.getDefault())
        selectedDate = getCurrentDate("dd-MMM-yyyy")
        binding?.etDischargeSummaryDate?.setText(getCurrentDate("dd-MMM-yyyy"))
        binding?.etDischargeSummaryDate?.setOnClickListener {
            openDatePicker()
        }

    }

    private fun initVisitOutPatientAdapter() {
        outPatientAdapter = OutPatientAdapter(requireContext())
        val tabletSize = resources.getBoolean(R.bool.isTablet)
        var gridLayoutManager: GridLayoutManager? = null
        if (tabletSize) {
            gridLayoutManager =
                GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        } else {
            gridLayoutManager =
                GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        }
        binding?.recyclerView?.layoutManager = gridLayoutManager
        binding?.recyclerView?.adapter = outPatientAdapter

        outPatientAdapter.setOnItemClickListener(object :
            OutPatientAdapter.OnItemClickListener {
            override fun onItemClick(responseContent: ResponseContent?, position: Int) {
                appPreferences?.saveInt(
                    AppConstants.PATIENT_UUID,
                    responseContent?.uuid!!
                )
                appPreferences?.saveString(
                    AppConstants.PATIENT_UHID,
                    responseContent?.uhid!!
                )
                appPreferences?.saveInt(AppConstants.ENCOUNTER_TYPE, AppConstants.TYPE_OUT_PATIENT)

                val emr = EmrWorkFlowActivity.newInstance(AppConstants.OUT_PATIENT)

                (activity as HomeActivity).replaceFragment(emr)

            }
        })

        binding?.recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (!isLoadingPaginationAdapterCallback) {
                        isLoadingPaginationAdapterCallback = true
                        currentPage += 1
                        if (currentPage <= TOTAL_PAGES) {
                            showCustomProgressDialog()
                            // Toast.makeText(requireContext(),""+currentPage,Toast.LENGTH_LONG).show()
                            viewModel?.getPatientListNextPage(
                                appPreferences?.getInt(AppConstants.FACILITY_UUID).toString(),
                                appPreferences?.getInt(AppConstants.DEPARTMENT_UUID).toString(),
                                queryvalue!!, selectedDate, currentPage,
                                patientSearchNextRetrofitCallBack

                            )
                        }

                    }

                }
            }
        })

    }

    private fun openDatePicker() {
        val currentDate = Calendar.getInstance()
        val mYear = currentDate[Calendar.YEAR] // current year
        val mMonth = currentDate[Calendar.MONTH] // current month
        val mDay = currentDate[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                startYear = year
                startMonthOfYear = monthOfYear
                startDayOfMonth = dayOfMonth
                // set day of month , month and year value in the edit text
                binding?.etDischargeSummaryDate!!.setText(
                    utils?.emrDisplayDate(
                        dayOfMonth.toString() +
                                "-" + (monthOfYear + 1) + "-" + year, "dd-MM-yyyy"
                    )
                )
                startDate[year, monthOfYear] = dayOfMonth
            },
            mYear, mMonth, mDay
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    fun callIntialSearch() {
        showCustomProgressDialog()
        viewModel?.searchPatient(
            appPreferences?.getInt(AppConstants.FACILITY_UUID).toString(),
            appPreferences?.getInt(AppConstants.DEPARTMENT_UUID).toString(), "",
            selectedDate,
            currentPage,
            pageSize,
            "modified_date",
            "DESC",
            patientSearchRetrofitCallBack
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 122 && resultCode == 144) {
            data?.getStringExtra("MESSAGE")?.let {
                outPatientAdapter.clearAll()
                departmentSearch = false
                showCustomProgressDialog()
                viewModel?.searchPatient(
                    appPreferences?.getInt(AppConstants.FACILITY_UUID).toString(),
                    appPreferences?.getInt(AppConstants.DEPARTMENT_UUID).toString(),
                    it,
                    selectedDate,
                    currentPage,
                    pageSize,
                    "modified_date",
                    "DESC",
                    patientSearchRetrofitCallBack
                )
            }

        }
    }

    val patientSearchRetrofitCallBack = object : RetrofitCallback<SearchResponseModel> {
        override fun onSuccessfulResponse(response: Response<SearchResponseModel>) {
            dismissCustomProgressDialog()
            val responsepatient = Gson().toJson(response.body()?.responseContents)

            outPatientAdapter.clearAll()

            if (response.body()?.responseContents?.size == 1 && !departmentSearch!!) {
                appPreferences?.saveInt(
                    AppConstants.PATIENT_UUID,
                    response.body()?.responseContents?.get(0)?.uuid!!
                )
                appPreferences?.saveInt(AppConstants.ENCOUNTER_TYPE, AppConstants.TYPE_OUT_PATIENT)

                val emr = EmrWorkFlowActivity.newInstance(AppConstants.OUT_PATIENT)

                (activity as HomeActivity).replaceFragment(emr)
                return
            }

            if (response.body()?.responseContents?.isNotEmpty()!!) {

//                viewModel?.errorTextVisibility?.value = 8
                binding?.llDropDownView?.visibility = View.GONE
                TOTAL_PAGES = Math.ceil(response.body()!!.totalRecords!!.toDouble() / 10).toInt()
                if (response.body()!!.responseContents!!.isNotEmpty()) {
                    isLoadingPaginationAdapterCallback = false
                    outPatientAdapter.addAll(response.body()!!.responseContents)
                    if (currentPage < TOTAL_PAGES) {
                        outPatientAdapter.addLoadingFooter()
//                        isLoading = true
                        //   setLoading(true)
                        isLastPage = false
                    } else {
                        outPatientAdapter.removeLoadingFooter()
//                        isLoading = false
                        // setLoading(false)
                        isLastPage = true
                    }

                } else {
                    outPatientAdapter.removeLoadingFooter()
//                    isLoading = false
                    //  setLoading(false)
                    isLastPage = true
                }
            } else {
                binding?.llDropDownView?.visibility = View.VISIBLE
                isLoadingPaginationAdapterCallback = false
                Toast.makeText(
                    requireContext(),
                    "" + "No data available",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        override fun onBadRequest(response: Response<SearchResponseModel>) {
            dismissCustomProgressDialog()
            isLoadingPaginationAdapterCallback = false
            val gson = GsonBuilder().create()
            val responseModel: SearchResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    SearchResponseModel::class.java
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
            dismissCustomProgressDialog()
            isLoadingPaginationAdapterCallback = false
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            dismissCustomProgressDialog()
            isLoadingPaginationAdapterCallback = false
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            dismissCustomProgressDialog()
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String) {
            dismissCustomProgressDialog()
            isLoadingPaginationAdapterCallback = false
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            dismissCustomProgressDialog()
            viewModel!!.progressBar.value = 8
        }
    }

    val patientSearchNextRetrofitCallBack = object : RetrofitCallback<SearchResponseModel> {
        override fun onSuccessfulResponse(response: Response<SearchResponseModel>) {
            if (response.body()?.responseContents!!.isNotEmpty()) {
                dismissCustomProgressDialog()
                outPatientAdapter.removeLoadingFooter()
//                isLoading = false
                // setLoading(false)
                isLoadingPaginationAdapterCallback = false
                outPatientAdapter.addAll(response.body()!!.responseContents)

                println("testing for two  = $currentPage--$TOTAL_PAGES")

                if (currentPage < TOTAL_PAGES) {
                    outPatientAdapter.addLoadingFooter()
//                    isLoading = true
                    //  setLoading(true)
                    isLastPage = false
                    println("testing for four  = $currentPage--$TOTAL_PAGES")
                } else {
                    isLastPage = true
//                    visitHistoryAdapter.removeLoadingFooter()
//                    isLoading = false
                    //   setLoading(false)
                    isLastPage = true
                    println("testing for five  = $currentPage--$TOTAL_PAGES")
                }
            } else {
                println("testing for six  = $currentPage--$TOTAL_PAGES")
                outPatientAdapter.removeLoadingFooter()
//                isLoading = false
                // setLoading(false)
                isLastPage = true
            }
        }

        override fun onBadRequest(response: Response<SearchResponseModel>?) {
            dismissCustomProgressDialog()
            isLoadingPaginationAdapterCallback = false
            outPatientAdapter.removeLoadingFooter()
//            isLoading = false
            // setLoading(false)
            isLastPage = true

        }

        override fun onServerError(response: Response<*>) {
            dismissCustomProgressDialog()
            isLoadingPaginationAdapterCallback = false
            viewModel!!.progressBar.value = View.GONE
            utils!!.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            dismissCustomProgressDialog()
            isLoadingPaginationAdapterCallback = false
            viewModel!!.progressBar.value = View.GONE
            utils!!.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onForbidden() {
            dismissCustomProgressDialog()
            viewModel!!.progressBar.value = View.GONE
            utils!!.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String) {
            dismissCustomProgressDialog()
            isLoadingPaginationAdapterCallback = false
            utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            dismissCustomProgressDialog()
            viewModel!!.progressBar.value = 8
        }
    }

    val oldSEarchResponseCallback = object : RetrofitCallback<OldPatientResponseModule> {

        override fun onSuccessfulResponse(responseBody: Response<OldPatientResponseModule>?) {

            val uhid = responseBody?.body()?.responseContent?.uhid
            if (uhid?.isNotEmpty()!!) {
                val responseContent: ResponseContent = ResponseContent()
                responseContent.first_name = responseBody.body()?.responseContent?.first_name
                responseContent.last_name = responseBody.body()?.responseContent?.last_name
                responseContent.age = responseBody.body()?.responseContent?.age
                responseContent.uhid = responseBody.body()?.responseContent?.uhid
                responseContent.patient_detail?.mobile =
                    responseBody.body()?.responseContent?.patient_detail?.mobile
                responseContent.uuid = responseBody.body()?.responseContent?.uuid
                outPatientAdapter.setData(responseContent)

            } else {

                val ft = childFragmentManager.beginTransaction()
                val managedialog = OldPatientSaveDialogFragment()
                val bundle = Bundle()
                bundle.putString(
                    AppConstants.OLDPINNUMBER,
                    responseBody.body()?.responseContent!!.old_pin.toString()
                )
                bundle.putInt(
                    AppConstants.AGE,
                    responseBody.body()?.responseContent!!.age!!
                )
                bundle.putString(
                    AppConstants.FirstTime,
                    responseBody.body()?.responseContent!!.first_name!!
                )
                bundle.putString(
                    AppConstants.GENDER,
                    responseBody.body()?.responseContent!!.gender
                )

                bundle.putString(
                    AppConstants.Createdate,
                    responseBody.body()?.responseContent!!.crt_dt
                )

                managedialog.arguments = bundle
                managedialog.show(ft, "Tag")

            }
        }

        override fun onBadRequest(errorBody: Response<OldPatientResponseModule>?) {
            val gson = GsonBuilder().create()
            val responseModel: OldPatientResponseModule
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    OldPatientResponseModule::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.statusCode!!.toString()
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
            viewModel!!.progressBar.value = 8
        }
    }


    private fun getCurrentDate(format: String): String {
        val currentDate = SimpleDateFormat(format, Locale.getDefault()).format(Date())
        return currentDate
    }

    fun showCustomProgressDialog() {
        customProgressDialog?.show()
    }

    fun dismissCustomProgressDialog() {
        customProgressDialog?.dismiss()
    }

}