package com.hmis_tn.doctor.ui.out_patient.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.callbacks.PaginationScrollListener
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.ActivityInPatientBinding
import com.hmis_tn.doctor.fire_base_analytics.AnalyticsManager
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.view.EmrWorkFlowActivity
import com.hmis_tn.doctor.ui.home.HomeActivity
import com.hmis_tn.doctor.ui.out_patient.search_response_model.InPatientResponseData
import com.hmis_tn.doctor.ui.out_patient.search_response_model.InPatientResponseModel
import com.hmis_tn.doctor.ui.out_patient.search_response_model.SearchResponseModel
import com.hmis_tn.doctor.ui.out_patient.view_model.InPatientViewModel
import com.hmis_tn.doctor.ui.out_patient.view_model.InPatientViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response

class InPatientActivity : Fragment() {
    private var binding: ActivityInPatientBinding? = null
    private var viewModel: InPatientViewModel? = null
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

    private var mCodeScanner: CodeScanner? = null

    companion object {
        private const val PERMISSION_REQUEST_CAMERA = 123
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.activity_in_patient,
                container,
                false
            )

        viewModel = InPatientViewModelFactory(
            requireActivity().application
        )
            .create(InPatientViewModel::class.java)
        binding?.lifecycleOwner = this
        // binding?.viewModel = viewModel
        utils = Utils(requireContext())

        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
        patientType = activity?.intent?.getStringExtra(AppConstants.PATIENT_TYPE)

        toolBarTitle()
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val searchText =
            binding?.searchView?.findViewById<TextView>(R.id.search_src_text)

        val tf = ResourcesCompat.getFont(requireContext(), R.font.poppins)
        searchText?.typeface = tf
        searchText?.text = ""
        initVisitOutPatientAdapter()

        trackDashBoardIPAnalyticsVisit()

        viewModel?.searchAdmittedPatients(
            "",
            currentPage,
            pageSize,
            "modified_date",
            "DESC",
            admittedPatientRetrofitCallback
        )
        binding?.searchView?.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            @SuppressLint("RestrictedApi")
            override fun onQueryTextSubmit(query: String): Boolean {
                callSearch(query)
                binding?.searchView?.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }

            fun callSearch(query: String) {
                queryvalue = query
                outPatientAdapter.clearAll()
                viewModel?.searchAdmittedPatients(
                    query,
                    currentPage,
                    pageSize,
                    "modified_date",
                    "DESC",
                    admittedPatientRetrofitCallback
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
        return binding!!.root
    }

    private fun toolBarTitle() {
        //  binding!!.toolbe.title = patientType
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
                    /*  appPreferences?.saveString(
                          AppConstants.PATIENT_UHID,
                          responseContent?.admission_pa!!
                      )*/
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

                    /*  val fragmentTransaction = childFragmentManager.beginTransaction()
                      fragmentTransaction.replace(R.id.landfragment, emr)
                      fragmentTransaction.addToBackStack(null)
                      fragmentTransaction.commit()*/

/*                startActivity(
                    Intent(requireContext(), EmrWorkFlowActivity::class.java)
                        .putExtra(
                            PATIENT_UUID,
                            responseContent?.patient_visits?.get(0)?.patient_uuid
                        )
                        .putExtra(AppConstants.ENCOUNTER_TYPE, TYPE_OUT_PATIENT)
                )*/

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
                    /*
                                        viewModel?.getPatientListNextPage(
                                            queryvalue!!,currentPage,
                                            patientSearchNextRetrofitCallBack

                                        )
                    */
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
            this.requireActivity().runOnUiThread {
                binding?.searchView!!.setQuery(result.text, true)
                Toast.makeText(requireContext(), result.text, Toast.LENGTH_SHORT)
                    .show()
                binding?.scannerView?.visibility = View.GONE
            }
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

    val patientSearchNextRetrofitCallBack = object : RetrofitCallback<InPatientResponseModel> {
        override fun onSuccessfulResponse(response: Response<InPatientResponseModel>) {
            if (response.body()?.responseContents!!.isNotEmpty()) {

                outPatientAdapter.removeLoadingFooter()
//                isLoading = false
                setLoading(false)

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

        override fun onBadRequest(response: Response<InPatientResponseModel>?) {
            outPatientAdapter.removeLoadingFooter()
//            isLoading = false
            setLoading(false)
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

    val admittedPatientRetrofitCallback = object: RetrofitCallback<InPatientResponseModel> {
        override fun onSuccessfulResponse(response: Response<InPatientResponseModel>) {

            if (response.body()?.responseContents?.isNotEmpty() == true) {
//                viewModel?.errorTextVisibility?.value = 8

                TOTAL_PAGES = Math.ceil(response.body()!!.totalRecords!!.toDouble() / 10).toInt()

                if (response.body()!!.responseContents!!.isNotEmpty()) {
                    outPatientAdapter.addAll(response.body()!!.responseContents)
                    if (currentPage < TOTAL_PAGES - 1) {
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
            }

        }

        override fun onBadRequest(response: Response<InPatientResponseModel>) {
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
            viewModel!!.progressBar.value = 8
        }
    }

    fun trackDashBoardIPAnalyticsVisit() {
        AnalyticsManager.getAnalyticsManager().trackDashBoardIPVisit()
    }

    private fun setLoading(toLoad: Boolean) {
        binding?.progressBar?.visibility = if (toLoad) View.VISIBLE else View.GONE
    }

}