package com.hmis_tn.doctor.ui.out_patient.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.callbacks.PaginationScrollListener
import com.hmis_tn.doctor.component.extention.hide
import com.hmis_tn.doctor.component.extention.isvisible
import com.hmis_tn.doctor.component.extention.show
import com.hmis_tn.doctor.component.extention.slideDown
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.FragmentTabMyPatientBinding
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.view.EmrWorkFlowActivity
import com.hmis_tn.doctor.ui.home.HomeActivity
import com.hmis_tn.doctor.ui.out_patient.search_response_model.MyPatientsResponseModel
import com.hmis_tn.doctor.ui.out_patient.search_response_model.MypatientResponseContent
import com.hmis_tn.doctor.ui.out_patient.view_model.OutPatientViewModel
import com.hmis_tn.doctor.ui.out_patient.view_model.OutPatientViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import com.hmis_tn.doctor.utils.custom_views.CustomProgressDialog
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class MyPatientTabFragment : Fragment() {

    private var customdialog: Dialog? = null
    private var typeDepartmentList = mutableMapOf<Int, String>()
    private var facility_UUID: Int? = 0
    private var depatment_id: Int? = 0
    private var doctor_id: Int? = 0
    private var Str_fromdate: String? = ""
    private var Str_todate: String? = ""
    private var fromdate: String? = ""
    private var todate: String? = ""
    private var fromDateRev: String = ""
    private var toDateRev: String = ""


    @SuppressLint("ClickableViewAccessibility")
    var binding: FragmentTabMyPatientBinding? = null
    private var viewModel: OutPatientViewModel? = null
    private var utils: Utils? = null
    lateinit var outPatientAdapter: MyPatientAdapter
    private var currentPage = 0
    private var pageSize = 10
    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES: Int = 0
    private var appPreferences: AppPreferences? = null

    private var patientType: String? = null
    private var exsistingPin: String = ""
    private var ufcNUmber: String = ""
    private var query: String = ""
    var customProgressDialog: CustomProgressDialog? = null


    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tab_my_patient,
                container,
                false
            )
        initViewmodel()
        initPerfences()
        initDropDown()
        initDateView()
        initClearAndSave()
        bindFirst()
        initVisitOutPatientAdapter()
        return binding!!.root
    }


    private fun initPerfences() {
        customProgressDialog = CustomProgressDialog(requireContext())
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        depatment_id = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        doctor_id = appPreferences?.getInt(AppConstants.DoctorUUID)
        utils = Utils(requireContext())
        patientType = activity?.intent?.getStringExtra(AppConstants.PATIENT_TYPE)

    }

    private fun initViewmodel() {
        viewModel = OutPatientViewModelFactory(
            requireActivity().application
        )
            .create(OutPatientViewModel::class.java)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
    }


    private fun bindFirst() {

        ufcNUmber = binding?.etUfcPin!!.text.trim().toString()
        exsistingPin = binding?.etExsistingPin!!.text.trim().toString()
        query = binding?.etSearch!!.text.trim().toString()


        val currentdata = Date()

        val formatter =
            SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

        val setdate = formatter.format(currentdata)

        Str_fromdate = setdate

        Str_todate = setdate

        binding?.FromDateEditText!!.setText(setdate)

        binding?.toDateEditText!!.setText(setdate)


        fromdate = utils!!.getDate(binding?.FromDateEditText!!.text.toString())
        fromdate = utils!!.convertDateFormat(
            fromdate!!,
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "EEE MMMM dd yyyy HH:mm"
        )
        Log.e("dateee", fromdate.toString())

        todate = utils!!.getDate(binding?.toDateEditText!!.text.toString())

        todate = utils!!.convertDateFormat(
            todate!!,
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "EEE MMMM dd yyyy HH:mm"
        )
        Log.e("dateee", todate.toString())

        showCustomProgressDialog()
        viewModel?.searchMyPatient(
            fromdate!!, todate!!, depatment_id!!.toString(),
            query,
            currentPage,
            pageSize,
            "modified_date",
            "DESC",
            patientSearchRetrofitCallBack
        )


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

    @SuppressLint("SetTextI18n")
    private fun initDateView() {
        Utils(requireContext()).setCalendarLocale("en", requireContext())
        binding?.FromDateEditText?.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    val c1 = Calendar.getInstance()
                    val mHour = c1[Calendar.HOUR_OF_DAY]
                    val mMinute = c1[Calendar.MINUTE]
                    val mSeconds = c1[Calendar.SECOND]

                    val timePickerDialog = TimePickerDialog(
                        this.activity,
                        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->

                            binding?.FromDateEditText?.setText(
                                String.format(
                                    "%02d",
                                    dayOfMonth
                                ) + "-" + String.format(
                                    "%02d",
                                    monthOfYear + 1
                                ) + "-" + year + " " + String.format(
                                    "%02d",
                                    hourOfDay
                                ) + ":" + String.format(
                                    "%02d",
                                    minute
                                ) + ":" + String.format(
                                    "%02d", mSeconds
                                )
                            )
                            Str_fromdate = binding?.FromDateEditText?.text.toString()


                        },
                        mHour,
                        mMinute,
                        false
                    )
                    timePickerDialog.show()

                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        binding?.toDateEditText?.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    val c1 = Calendar.getInstance()
                    val mHour = c1[Calendar.HOUR_OF_DAY]
                    val mMinute = c1[Calendar.MINUTE]
                    val mSeconds = c1[Calendar.SECOND]

                    val timePickerDialog = TimePickerDialog(
                        this.activity,
                        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->

                            binding?.toDateEditText?.setText(
                                String.format(
                                    "%02d",
                                    dayOfMonth
                                ) + "-" + String.format(
                                    "%02d",
                                    monthOfYear + 1
                                ) + "-" + year + " " + String.format(
                                    "%02d",
                                    hourOfDay
                                ) + ":" + String.format(
                                    "%02d",
                                    minute
                                ) + ":" + String.format(
                                    "%02d", mSeconds
                                )
                            )
                            Str_todate = binding?.toDateEditText?.text.toString()


                        },
                        mHour,
                        mMinute,
                        false
                    )
                    timePickerDialog.show()

                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }
    }

    private fun initClearAndSave() {
        binding?.clearcardview?.setOnClickListener {
            clearFilterData()
        }
        binding?.searchButton!!.setOnClickListener {

            outPatientAdapter.clearAll()
            if (binding?.FromDateEditText!!.text?.trim().toString().isNotEmpty()) {
                fromdate = utils!!.getDate(binding?.FromDateEditText!!.text.toString())

                val formatter =
                    SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                val dateToDisplay = formatter.parse(Str_fromdate)
                val dt1 = SimpleDateFormat("EEE MMMM dd yyyy HH:mm")

                fromdate = dt1.format(dateToDisplay)
                Log.e("dateee", fromdate.toString())

            }
            if (binding?.toDateEditText!!.text?.trim().toString().isNotEmpty()) {
                todate = utils!!.getDate(binding?.toDateEditText!!.text.toString())
                val formatter =
                    SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                val dateToDisplay = formatter.parse(Str_todate)
                val dt1 = SimpleDateFormat("EEE MMMM dd yyyy HH:mm")

                todate = dt1.format(dateToDisplay)

                Log.e("dateee", todate.toString())
            }


            ufcNUmber = binding?.etUfcPin!!.text.trim().toString()
            exsistingPin = binding?.etExsistingPin!!.text.trim().toString()
            query = binding?.etSearch!!.text.trim().toString()

            viewModel?.searchMyPatient(
                fromdate!!, todate!!, depatment_id!!.toString(),
                query,
                currentPage,
                pageSize,
                "modified_date",
                "DESC",
                patientSearchRetrofitCallBack
            )
        }
    }

    private fun initVisitOutPatientAdapter() {
        outPatientAdapter = MyPatientAdapter(requireContext())
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
            MyPatientAdapter.OnItemClickListener {
            override fun onItemClick(responseContent: MypatientResponseContent?, position: Int) {
                appPreferences?.saveInt(
                    AppConstants.PATIENT_UUID,
                    responseContent?.patient_uuid!!
                )
                appPreferences?.saveInt(AppConstants.ENCOUNTER_TYPE, AppConstants.TYPE_OUT_PATIENT)

                val emr = EmrWorkFlowActivity.newInstance(AppConstants.OUT_PATIENT)

                (activity as HomeActivity).replaceFragment(emr)
            }
        })
        binding?.recyclerView?.addOnScrollListener(object :
            PaginationScrollListener(gridLayoutManager) {

            override fun loadMoreItems() {
                println("bindingdsafgsethry = ${binding}")
                currentPage += 1
                if (currentPage <= TOTAL_PAGES) {
                    viewModel?.getMyPatientListNextPage(
                        fromdate!!, todate!!,
                        depatment_id.toString(),
                        query, currentPage,
                        patientSearchNextRetrofitCallBack

                    )
                }
            }

            override fun getTotalPageCount(): Int {
                return TOTAL_PAGES
            }

            override fun isLastPageReached(): Boolean {
                return isLastPage
            }

            override fun isLoadingHappening(): Boolean {
                println("isLoadingdefwregwtrhey = ${isLoading}")
                return isLoading
            }
        })
    }

    val patientSearchRetrofitCallBack = object : RetrofitCallback<MyPatientsResponseModel> {

        override fun onSuccessfulResponse(response: Response<MyPatientsResponseModel>) {
            if (response.body()?.responseContents?.isNotEmpty()!!) {
                TOTAL_PAGES = Math.ceil(response.body()!!.totalRecords.toDouble() / 10).toInt()

                if (response.body()!!.responseContents.isNotEmpty()) {
                    outPatientAdapter.addAll(response.body()!!.responseContents)
                    if (currentPage < TOTAL_PAGES) {
                        outPatientAdapter.addLoadingFooter()
                        isLoading = true
                        isLastPage = false
                    } else {
                        outPatientAdapter.removeLoadingFooter()
                        isLoading = false
                        isLastPage = true
                    }

                } else {
                    outPatientAdapter.removeLoadingFooter()
                    isLoading = false
                    isLastPage = true
                }
            }

        }

        override fun onBadRequest(response: Response<MyPatientsResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: MyPatientsResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    MyPatientsResponseModel::class.java
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
            dismissCustomProgressDialog()
            viewModel!!.progressBar.value = 8
        }
    }

    val patientSearchNextRetrofitCallBack = object : RetrofitCallback<MyPatientsResponseModel> {
        override fun onSuccessfulResponse(response: Response<MyPatientsResponseModel>) {
            if (response.body()?.responseContents!!.isNotEmpty()) {

                outPatientAdapter.removeLoadingFooter()
                isLoading = false

                outPatientAdapter.addAll(response.body()!!.responseContents)

                println("testing for two  = $currentPage--$TOTAL_PAGES")

                if (currentPage < TOTAL_PAGES) {
                    outPatientAdapter.addLoadingFooter()
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
                outPatientAdapter.removeLoadingFooter()
                isLoading = false
                isLastPage = true
            }
        }

        override fun onBadRequest(response: Response<MyPatientsResponseModel>?) {
            outPatientAdapter.removeLoadingFooter()
            isLoading = false
            isLastPage = true

        }

        override fun onServerError(response: Response<*>) {
            viewModel!!.progressBar.value = View.GONE
            utils!!.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
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
            utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progressBar.value = 8
        }
    }

    fun clearFilterData() {
        setCurrentData()
    }

    fun setCurrentData() {
        val currentdata = Date()

        val formatter =
            SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

        val setdate = formatter.format(currentdata)

        Str_fromdate = setdate

        Str_todate = setdate

        binding?.FromDateEditText!!.setText(setdate)

        binding?.toDateEditText!!.setText(setdate)
    }

    fun showCustomProgressDialog() {
        customProgressDialog?.show()
    }

    fun dismissCustomProgressDialog() {
        customProgressDialog?.dismiss()
    }


}