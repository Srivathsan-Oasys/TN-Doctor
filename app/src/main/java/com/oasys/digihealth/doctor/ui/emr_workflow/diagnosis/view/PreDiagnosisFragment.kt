package com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.PrevDiagnosisFragmentBinding
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.model.search_complaint.PrevChiefComplaintResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.model.PreDiagnosisResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view_model.DiagnosisViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view_model.DiagnosisViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response


class PreDiagnosisFragment : Fragment() {
    private var binding: PrevDiagnosisFragmentBinding? = null
    private var viewModel: DiagnosisViewModel? = null
    private var patientID: Int? = 0
    var mCallback: CancelListener? = null
    private var utils: Utils? = null
    private var patientUuid: Int = 0
    private var mAdapter: PrevDiagnosisAdapter? = null
    private var mAdapterMob: PrevDiagnosisMobileAdapter? = null
    var appPreferences: AppPreferences? = null
    private var mainCallback: diagnosisfrevClickedListener? = null

    private var IsTablet = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.prev_diagnosis_fragment,
                container,
                false
            )

        viewModel = DiagnosisViewModelFactory(
            requireActivity().application
        ).create(DiagnosisViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        utils = Utils(requireContext())

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val patientID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        val facilityid = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        val encounter_uuid = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)

        val encounter_Type = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)

        IsTablet = resources.getBoolean(R.bool.isTablet)

        viewModel?.getPrevDiagnosisList(
            patientID,
            facilityid,
            encounter_Type,
            prevDiagnosisrecordsRetrofitCallback
        )

        if (IsTablet)
            preparePatientLIstData()
        else
            preparePatientLIstMobileData()

        binding?.cancelcardview?.setOnClickListener {

            mCallback?.cancelfunction()
        }


        return binding!!.root
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (menuVisible) {
            trackPrevDiagnosisVisit(utils!!.getEncounterType())
        }
    }

    val prevDiagnosisrecordsRetrofitCallback =
        object : RetrofitCallback<PreDiagnosisResponseModel> {
            override fun onSuccessfulResponse(response: Response<PreDiagnosisResponseModel>) {

                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    viewModel?.errorTextVisibility?.value = 8
                    if (IsTablet)
                        mAdapter?.refreshList((response.body()?.responseContents)!!)
                    else
                        mAdapterMob?.refreshList((response.body()?.responseContents)!!)

                } else {
                    viewModel?.errorTextVisibility?.value = 0
                }

            }

            override fun onBadRequest(response: Response<PreDiagnosisResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: PreDiagnosisResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        PreDiagnosisResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        response.message()
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

//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private fun preparePatientLIstData() {
        mAdapter = PrevDiagnosisAdapter((requireActivity()))
        binding?.daignosisRecyclerView!!.adapter = mAdapter


    }

    private fun preparePatientLIstMobileData() {
        mAdapterMob = PrevDiagnosisMobileAdapter((requireActivity()))
        binding?.daignosisRecyclerView!!.adapter = mAdapterMob


    }


    interface diagnosisfrevClickedListener {
        fun sendPrevtoChild(
            responseContent: ArrayList<PrevChiefComplaintResponseContent?>?
        )
    }

    fun setOnTextClickedListener(callback: diagnosisfrevClickedListener) {
        this.mainCallback = callback
    }


    override fun onResume() {
        super.onResume()
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val patientID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        val facilityid = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        val encounter_uuid = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)

        val encounter_Type = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)

        viewModel?.getPrevDiagnosisList(
            patientID,
            facilityid,
            encounter_Type,
            prevDiagnosisrecordsRetrofitCallback
        )
    }

    fun trackPrevDiagnosisVisit(type: String) {
        AnalyticsManager.getAnalyticsManager().trackPrevDiagnosisVisit(type)
    }

    //defining Interface
    interface CancelListener {
        fun cancelfunction(

        )
    }

    fun setOnTextClickedListener(callback: CancelListener) {
        this.mCallback = callback
    }
}
