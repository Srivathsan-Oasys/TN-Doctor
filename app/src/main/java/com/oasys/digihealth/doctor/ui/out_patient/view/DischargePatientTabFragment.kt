package com.oasys.digihealth.doctor.ui.out_patient.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.callbacks.PaginationScrollListener
import com.oasys.digihealth.doctor.component.extention.hide
import com.oasys.digihealth.doctor.component.extention.isvisible
import com.oasys.digihealth.doctor.component.extention.show
import com.oasys.digihealth.doctor.component.extention.slideDown
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentTabDischargePatientBinding
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.view.EmrWorkFlowActivity
import com.oasys.digihealth.doctor.ui.home.HomeActivity
import com.oasys.digihealth.doctor.ui.out_patient.search_response_model.InPatientResponseData
import com.oasys.digihealth.doctor.ui.out_patient.search_response_model.InPatientResponseModel
import com.oasys.digihealth.doctor.ui.out_patient.search_response_model.SearchResponseModel
import com.oasys.digihealth.doctor.ui.out_patient.view_model.DischargePatientViewModel
import com.oasys.digihealth.doctor.ui.out_patient.view_model.DischargePatientViewModelFactory
import com.oasys.digihealth.doctor.ui.scanner.ScannerActivity
import com.oasys.digihealth.doctor.utils.Utils
import com.oasys.digihealth.doctor.utils.custom_views.CustomProgressDialog
import retrofit2.Response


class DischargePatientTabFragment : Fragment() {

    private var customdialog: Dialog? = null
    private var typeDepartmentList = mutableMapOf<Int, String>()
    private var facility_UUID: Int? = 0

    @SuppressLint("ClickableViewAccessibility")
    var binding: FragmentTabDischargePatientBinding? = null
    private var department_uuid: Int? = null
    private var viewModel: DischargePatientViewModel? = null
    private var utils: Utils? = null
    lateinit var outPatientAdapter: InpatientAdapter
    private var currentPage = 0
    private var pageSize = 10
    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES: Int = 0
    private var appPreferences: AppPreferences? = null
    private var queryvalue: String? = ""
    private var patientType: String? = null
    var customProgressDialog: CustomProgressDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tab_discharge_patient,
                container,
                false
            )

        initViewModel()
        initPerefences()
        initDropDown()
        initVisitOutPatientAdapter()
        showCustomProgressDialog()
        viewModel?.searchDischargePatients(
            "",
            currentPage,
            pageSize,
            "admission_date",
            "ASC",
            admittedPatientRetrofitCallback
        )



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
                if (binding?.etSearch?.text.toString().trim().length >= 3) {
                    queryvalue = binding?.etSearch?.text.toString()
                    outPatientAdapter.clearAll()
                    showCustomProgressDialog()
                    viewModel?.searchDischargePatients(
                        binding?.etSearch?.text.toString(),
                        currentPage,
                        pageSize,
                        "modified_date",
                        "DESC",
                        admittedPatientRetrofitCallback
                    )
                }
            }
            true
        }

        binding?.searchButton?.setOnClickListener {
            val searchview = binding?.etSearch?.text.toString()
            if (searchview.isNotEmpty() && searchview.length >= 3) {
                showCustomProgressDialog()
                viewModel?.searchDischargePatients(
                    binding?.etSearch?.text.toString(),
                    currentPage,
                    pageSize,
                    "modified_date",
                    "DESC",
                    admittedPatientRetrofitCallback
                )
            }
        }

        binding?.clearcardview?.setOnClickListener {
            showCustomProgressDialog()
            viewModel?.searchDischargePatients(
                "",
                currentPage,
                pageSize,
                "admission_date",
                "ASC",
                admittedPatientRetrofitCallback
            )
        }
        binding?.qrCodeImageview?.setOnClickListener {
            val intent = Intent(requireActivity(), ScannerActivity::class.java)
            startActivityForResult(intent, 122)
        }
        return binding!!.root
    }
    //defining Interface

    fun initPerefences() {
        customProgressDialog = CustomProgressDialog(requireContext())
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        utils = Utils(requireContext())
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        patientType = activity?.intent?.getStringExtra(AppConstants.PATIENT_TYPE)


    }

    fun initViewModel() {
        viewModel = DischargePatientViewModelFactory(
            requireActivity().application
        ).create(DischargePatientViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this

        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
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


    private fun initVisitOutPatientAdapter() {
        outPatientAdapter = InpatientAdapter(requireContext())
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
            InpatientAdapter.OnItemClickListener {
            override fun onItemClick(responseContent: InPatientResponseData?, position: Int) {
                try {
                    appPreferences?.saveInt(
                        AppConstants.PATIENT_UUID,
                        responseContent?.admission_patient_uuid!!
                    )
                    appPreferences?.saveInt(
                        AppConstants.ENCOUNTER_TYPE,
                        AppConstants.TYPE_IN_PATIENT
                    )

                    appPreferences?.saveInt(
                        AppConstants.WARDUUID,
                        responseContent?.admission_ward_uuid!!
                    )

                    val emr = EmrWorkFlowActivity.newInstance(AppConstants.IN_PATIENT)

                    (activity as HomeActivity).replaceFragment(emr)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })

        binding?.recyclerView?.addOnScrollListener(object :
            PaginationScrollListener(gridLayoutManager) {

            override fun loadMoreItems() {
                println("bindingdsafgsethry = ${binding}")
                currentPage += 1
                if (currentPage <= TOTAL_PAGES) {
                    viewModel?.getPatientListNextPage(
                        queryvalue!!, currentPage,
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


    val admittedPatientRetrofitCallback = object : RetrofitCallback<InPatientResponseModel> {
        override fun onSuccessfulResponse(response: Response<InPatientResponseModel>) {
            dismissCustomProgressDialog()
            outPatientAdapter.addAll(response.body()!!.responseContents!!)

        }

        override fun onBadRequest(response: Response<InPatientResponseModel>) {
            dismissCustomProgressDialog()
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
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            dismissCustomProgressDialog()
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
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            dismissCustomProgressDialog()
            viewModel!!.progressBar.value = 8
        }
    }
    val patientSearchNextRetrofitCallBack = object : RetrofitCallback<InPatientResponseModel> {
        override fun onSuccessfulResponse(response: Response<InPatientResponseModel>) {
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

        override fun onBadRequest(response: Response<InPatientResponseModel>?) {
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


    fun trackDashboardIPDischargeAnalyticsVisit() {
        AnalyticsManager.getAnalyticsManager().trackDashboardIPDischargeVisit()
    }

    fun showCustomProgressDialog() {
        customProgressDialog?.show()
    }

    fun dismissCustomProgressDialog() {
        customProgressDialog?.dismiss()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 122 && resultCode == 144) {
            data?.getStringExtra("MESSAGE")?.let {
                viewModel?.searchDischargePatients(
                    it,
                    currentPage,
                    pageSize,
                    "admission_date",
                    "ASC",
                    admittedPatientRetrofitCallback
                )
            }

        }
    }


}