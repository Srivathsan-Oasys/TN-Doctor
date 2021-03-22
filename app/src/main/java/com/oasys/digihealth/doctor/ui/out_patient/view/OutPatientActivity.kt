package com.oasys.digihealth.doctor.ui.out_patient.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.zxing.integration.android.IntentIntegrator
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.callbacks.FragmentBackClick
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppConstants.TYPE_OUT_PATIENT
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.ActivityOutPatientBinding
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.view.EmrWorkFlowActivity
import com.oasys.digihealth.doctor.ui.home.HomeActivity
import com.oasys.digihealth.doctor.ui.out_patient.search_response_model.OldPatientResponseModule
import com.oasys.digihealth.doctor.ui.out_patient.search_response_model.ResponseContent
import com.oasys.digihealth.doctor.ui.out_patient.search_response_model.SearchResponseModel
import com.oasys.digihealth.doctor.ui.out_patient.view_model.OutPatientViewModel
import com.oasys.digihealth.doctor.ui.out_patient.view_model.OutPatientViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OutPatientActivity : Fragment(), OldPatientSaveDialogFragment.OnOldPatientProcessListener {
    private var searchEdit: EditText? = null
    private var binding: ActivityOutPatientBinding? = null
    private var viewModel: OutPatientViewModel? = null
    private var utils: Utils? = null
    lateinit var outPatientAdapter: OutPatientAdapter
    private var currentPage = 0
    private var pageSize = 10
    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES: Int = 0
    private var appPreferences: AppPreferences? = null
    private var queryvalue: String? = ""
    private var patientType: String? = null
    private var facility_UUID: Int? = 0
    private var responseContentList: ArrayList<ResponseContent?>? = ArrayList()
    private var fragmentBackClick: FragmentBackClick? = null
    private var isLoadingPaginationAdapterCallback: Boolean = false

    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null
    private var mHour: Int? = null
    private var mMinute: Int? = null
    private var mSecond: Int? = null
    private var selectedDate: String = ""

    //qr code scanner object
    private var qrScan: IntentIntegrator? = null
    private var mCodeScanner: CodeScanner? = null
    var jsonObj: JSONObject = JSONObject()

    companion object {
        const val PERMISSION_REQUEST_CAMERA = 123
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.activity_out_patient,
                container,
                false
            )
        viewModel = OutPatientViewModelFactory(
            requireActivity().application
        )
            .create(OutPatientViewModel::class.java)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        utils = Utils(requireContext())
//intializing scan object
        if (activity !is FragmentBackClick) {
//            throw ClassCastException("Hosting activity must implement BackHandlerInterface")
        } else {
            fragmentBackClick = activity as FragmentBackClick?
        }
        qrScan = IntentIntegrator(requireActivity())

        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
        patientType = activity?.intent?.getStringExtra(AppConstants.PATIENT_TYPE)
        toolBarTitle()
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        binding?.favouriteDrawerCardView?.setOnClickListener {
            binding?.drawerLayout!!.openDrawer(GravityCompat.END)
        }
        binding?.drawerLayout?.drawerElevation = 0f
        binding?.drawerLayout?.setScrimColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )


        searchEdit =
            binding?.searchView?.findViewById(R.id.search_src_text)

        val tf = ResourcesCompat.getFont(requireContext(), R.font.poppins)
        searchEdit!!.typeface = tf
        initVisitOutPatientAdapter()
        binding?.searchView?.requestFocus()
        searchEdit!!.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(15))
        binding?.searchView!!.isFocusable = true
        binding?.searchView!!.isIconified = false
        binding?.searchView!!.requestFocusFromTouch()
        binding?.scannerView?.visibility = View.GONE
        trackDashBoardOPSearchAnalyticsVisit()

        binding?.searchView?.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            @SuppressLint("RestrictedApi")
            override fun onQueryTextSubmit(query: String): Boolean {
                callSearch(query)
                binding?.searchView?.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.length > 0) {
                    binding!!.pinview.setQuery("", false)
                }

                if (newText.length <= 9) {
                    searchEdit!!.error = "Please enter minimum 10 chracters"

                    return false
                }
                return true
            }

            fun callSearch(query: String) {
                currentPage = 0
                queryvalue = query
                outPatientAdapter.clearAll()
                val view: View = requireActivity().currentFocus!!
                if (view != null) {
                    val imm: InputMethodManager =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                viewModel?.searchPatient(
                    "",
                    "",
                    query,
                    selectedDate,
                    currentPage,
                    pageSize,
                    "modified_date",
                    "DESC",
                    patientSearchRetrofitCallBack
                )
            }

        })


        binding?.qrCodeImageview?.setOnClickListener {
            binding?.scannerView?.visibility = View.VISIBLE

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                runTimePermission()

            } else {
                startScanning()
            }
        }

        binding?.pinview?.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            @SuppressLint("RestrictedApi")
            override fun onQueryTextSubmit(query: String): Boolean {
                callSearchpin(query)
                binding?.pinview?.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    binding?.searchView?.setQuery("", false)
                }

                return true
            }

            fun callSearchpin(query: String) {
                outPatientAdapter.clearAll()
                val view: View = requireActivity().currentFocus!!
                if (view != null) {
                    val imm: InputMethodManager =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }

                viewModel?.getOldPatient(
                    query,
                    facility_UUID,
                    oldSEarchResponseCallback
                )
            }

        })

        binding!!.searchButton.setOnClickListener {
            try {
                val view: View = requireActivity().currentFocus!!
                if (view != null) {
                    val imm: InputMethodManager =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            } catch (e: Exception) {

            }

            val pinnumber = binding?.pinview!!.query.toString()
            val searchview = binding?.searchView!!.query.toString()
            if (pinnumber.isNotEmpty()) {
                outPatientAdapter.clearAll()

                viewModel?.getOldPatient(
                    pinnumber,
                    facility_UUID,
                    oldSEarchResponseCallback
                )
                return@setOnClickListener
            } else if (searchview.isNotEmpty()) {
                currentPage = 0
                queryvalue = searchview
                outPatientAdapter.clearAll()
                viewModel?.searchPatient(
                    "",
                    "",
                    searchview,
                    selectedDate,
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
        val sdf = SimpleDateFormat("dd-mm-yyy HH:mm:ss", Locale.getDefault())
        selectedDate = getCurrentDate("dd-MMM-yyyy")
        binding?.etDischargeSummaryDate?.setText(getCurrentDate("dd-MMM-yyyy"))
        binding?.etDischargeSummaryDate?.setOnClickListener {
            openDatePicker()
        }

        binding?.clearcardview?.setOnClickListener {
            binding?.pinview!!.setQuery("", false)
            binding?.searchView!!.setQuery("", false)
        }
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
        return binding!!.root
    }


    private fun getCurrentDate(format: String): String {
        val currentDate = SimpleDateFormat(format, Locale.getDefault()).format(Date())
        return currentDate
    }

    private fun openDatePicker() {
        val sdf = SimpleDateFormat("dd-MM-yyy HH:mm:ss", Locale.getDefault())
        val calendarDisplay: Calendar = Calendar.getInstance()
        val calendar: Calendar = Calendar.getInstance()
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this.requireActivity(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

                val cal = Calendar.getInstance()
                mHour = cal[Calendar.HOUR_OF_DAY]
                mMinute = cal[Calendar.MINUTE]
                mSecond = cal[Calendar.SECOND]

                calendarDisplay.set(Calendar.YEAR, year)
                calendarDisplay.set(Calendar.MONTH, monthOfYear + 1)
                calendarDisplay.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val timePickerDialog = TimePickerDialog(
                    this.activity,
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                        calendarDisplay.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendarDisplay.set(Calendar.MINUTE, minute)
                        val displayDischargeDate = sdf.format(calendarDisplay.time)
                        //Passing to API
                        selectedDate = displayDischargeDate
                        //  dischargeAPIDate = formatter.format(calendarDisplay.time)
                        //generateAPIDate = formatter.format(Date().time)
                        //Display the UI Date
                        binding?.etDischargeSummaryDate?.setText(displayDischargeDate)
                    },
                    mHour!!,
                    mMinute!!,
                    false
                )
                timePickerDialog.show()
            },
            mYear!!,
            mMonth!!,
            mDay!!
        )
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    private fun runTimePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.CAMERA
                ), PERMISSION_REQUEST_CAMERA
            )
            return
        } else {
            startScanning()
            return
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {


            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // now, you have permission go ahead
                Toast.makeText(requireContext(), "Camera permission granted", Toast.LENGTH_LONG)
                    .show()
                startScanning()

            } else {
                getCustomDialog()
            }

        }

    }

    private fun startScanning() {
        if (mCodeScanner == null) {
            mCodeScanner = CodeScanner(requireContext(), binding!!.scannerView)
        }


        if (mCodeScanner != null) {
            mCodeScanner!!.startPreview()
        }
        mCodeScanner!!.decodeCallback = DecodeCallback { result ->
            this.requireActivity().runOnUiThread(Runnable {
                binding?.searchView!!.setQuery(result.text, true)
                Toast.makeText(requireContext(), result.text, Toast.LENGTH_SHORT)
                    .show()

                binding?.scannerView?.visibility = View.GONE
            })
            if (mCodeScanner != null) {
                mCodeScanner!!.releaseResources()

            }
        }

    }

    private fun getCustomDialog() {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(context)
        // set message of alert dialog
        dialogBuilder.setMessage("App need this permission")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Proceed", DialogInterface.OnClickListener { dialog, id ->
                runTimePermission()
            })
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Permission!!")
        // show alert dialog
        alert.show()
    }


    private fun toolBarTitle() {
        //  binding!!.toolbe.title = patientType
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
                appPreferences?.saveInt(AppConstants.ENCOUNTER_TYPE, TYPE_OUT_PATIENT)

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

    val patientSearchRetrofitCallBack = object : RetrofitCallback<SearchResponseModel> {

        override fun onSuccessfulResponse(response: Response<SearchResponseModel>) {

            val responsepatient = Gson().toJson(response.body()?.responseContents)

            outPatientAdapter.clearAll()

            if (response.body()?.responseContents?.size == 1) {
                appPreferences?.saveInt(
                    AppConstants.PATIENT_UUID,
                    response.body()?.responseContents?.get(0)?.uuid!!
                )
                appPreferences?.saveInt(AppConstants.ENCOUNTER_TYPE, TYPE_OUT_PATIENT)

                val emr = EmrWorkFlowActivity.newInstance(AppConstants.OUT_PATIENT)

                (activity as HomeActivity).replaceFragment(emr)
                return
            }

            if (response.body()?.responseContents?.isNotEmpty()!!) {

//                viewModel?.errorTextVisibility?.value = 8
                TOTAL_PAGES = Math.ceil(response.body()!!.totalRecords!!.toDouble() / 10).toInt()
                if (response.body()!!.responseContents!!.isNotEmpty()) {
                    isLoadingPaginationAdapterCallback = false
                    outPatientAdapter.addAll(response.body()!!.responseContents)
                    if (currentPage < TOTAL_PAGES) {
                        outPatientAdapter.addLoadingFooter()
//                        isLoading = true
                        setLoading(true)
                        isLastPage = false
                    } else {
                        outPatientAdapter.removeLoadingFooter()
//                        isLoading = false
                        setLoading(false)
                        isLastPage = true
                    }

                } else {
                    outPatientAdapter.removeLoadingFooter()
//                    isLoading = false
                    setLoading(false)
                    isLastPage = true
                }
            } else {
                isLoadingPaginationAdapterCallback = false
                Toast.makeText(
                    requireContext(),
                    "" + "No data available in table",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        override fun onBadRequest(response: Response<SearchResponseModel>) {
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
            viewModel!!.progressBar.value = 8
        }
    }

    val patientSearchNextRetrofitCallBack = object : RetrofitCallback<SearchResponseModel> {
        override fun onSuccessfulResponse(response: Response<SearchResponseModel>) {
            if (response.body()?.responseContents!!.isNotEmpty()) {

                outPatientAdapter.removeLoadingFooter()
//                isLoading = false
                setLoading(false)
                isLoadingPaginationAdapterCallback = false
                outPatientAdapter.addAll(response.body()!!.responseContents)

                println("testing for two  = $currentPage--$TOTAL_PAGES")

                if (currentPage < TOTAL_PAGES) {
                    outPatientAdapter.addLoadingFooter()
//                    isLoading = true
                    setLoading(true)
                    isLastPage = false
                    println("testing for four  = $currentPage--$TOTAL_PAGES")
                } else {
                    isLastPage = true
//                    visitHistoryAdapter.removeLoadingFooter()
//                    isLoading = false
                    setLoading(false)
                    isLastPage = true
                    println("testing for five  = $currentPage--$TOTAL_PAGES")
                }
            } else {
                println("testing for six  = $currentPage--$TOTAL_PAGES")
                outPatientAdapter.removeLoadingFooter()
//                isLoading = false
                setLoading(false)
                isLastPage = true
            }
        }

        override fun onBadRequest(response: Response<SearchResponseModel>?) {
            isLoadingPaginationAdapterCallback = false
            outPatientAdapter.removeLoadingFooter()
//            isLoading = false
            setLoading(false)
            isLastPage = true

        }

        override fun onServerError(response: Response<*>) {
            isLoadingPaginationAdapterCallback = false
            viewModel!!.progressBar.value = View.GONE
            utils!!.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            isLoadingPaginationAdapterCallback = false
            viewModel!!.progressBar.value = View.GONE
            utils!!.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onForbidden() {
            viewModel!!.progressBar.value = View.GONE
            utils!!.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String) {
            isLoadingPaginationAdapterCallback = false
            utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progressBar.value = 8
        }
    }

    override fun onResume() {
        super.onResume()
        if (mCodeScanner != null) {
            mCodeScanner!!.startPreview()
        }
    }

    override fun onPause() {
        if (mCodeScanner != null) {
            mCodeScanner!!.releaseResources()
        }
        super.onPause()
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

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        if (childFragment is OldPatientSaveDialogFragment) {
            childFragment.setRefreshOldPatientListener(this)
        }

    }

    override fun onRefreshOldPatientList(responseContent: ResponseContent) {
        outPatientAdapter.setData(responseContent)
    }

    fun trackDashBoardOPSearchAnalyticsVisit() {
        AnalyticsManager.getAnalyticsManager().trackDashBoardOPSearchVisit()
    }

    private fun setLoading(toLoad: Boolean) {
        binding?.progressBar?.visibility = if (toLoad) View.VISIBLE else View.GONE
    }

    override fun onStart() {
        super.onStart()
        fragmentBackClick?.setSelectedFragment(this)
    }
}