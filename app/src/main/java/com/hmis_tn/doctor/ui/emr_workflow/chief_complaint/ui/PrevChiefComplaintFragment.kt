package com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.extention.isTablet
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.PrevChiefComplaintFragmentBinding
import com.hmis_tn.doctor.fire_base_analytics.AnalyticsManager
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.PrevChiefComplainResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.search_complaint.PrevChiefComplaintResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.view_model.ChiefComplaintViewModel
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.view_model.ChiefComplaintViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response


class PrevChiefComplaintFragment : Fragment() {

    private var binding: PrevChiefComplaintFragmentBinding? = null
    private var viewModel: ChiefComplaintViewModel? = null
    private var prevChiefResponse: ArrayList<PrevChiefComplaintResponseContent?>? = null
    private var utils: Utils? = null
    private var patientUuid: Int = 0
    var mCallback: CancelListener? = null
    private var mAdapter: PrevChiefComplaintAdapter? = null
    var appPreferences: AppPreferences? = null
    private var mainCallback: ChiefrevClickedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.prev_chief_complaint_fragment,
                container,
                false
            )

        viewModel = ChiefComplaintViewModelFactory(
            requireActivity().application
        ).create(ChiefComplaintViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        utils = Utils(requireContext())

        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val patientID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        val facilityid = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        val encounter_uuid = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
        viewModel?.getPrevChiefComplaintList(
            patientID,
            facilityid,
            encounter_uuid,
            prevlabrecordsRetrofitCallback
        )
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
        preparePatientLIstData()
        binding?.cancelCardView?.setOnClickListener {
            mCallback?.cancelfunction()
        }
        return binding!!.root
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (menuVisible) {
            trackPrevChiefAnalyticsVisit(utils!!.getEncounterType())
        }
    }

    val prevlabrecordsRetrofitCallback = object : RetrofitCallback<PrevChiefComplainResponseModel> {
        override fun onSuccessfulResponse(response: Response<PrevChiefComplainResponseModel>) {

            if (response.body()?.responseContents?.isNotEmpty()!!) {

                mAdapter?.refreshList((response.body()?.responseContents)!!)

            }

        }

        override fun onBadRequest(response: Response<PrevChiefComplainResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: PrevChiefComplainResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    PrevChiefComplainResponseModel::class.java
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
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    private fun preparePatientLIstData() {
        mAdapter = PrevChiefComplaintAdapter((requireActivity()))
        if (isTablet(requireContext())) {
            binding?.previewRecyclerView!!.adapter = mAdapter

        } else {
            binding?.previewMobileRecyclerView!!.adapter = mAdapter

        }


    }

    override fun onResume() {
        super.onResume()
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val patientID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        val facilityid = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        val encounter_uuid = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)

        viewModel?.getPrevChiefComplaintList(
            patientID,
            facilityid,
            encounter_uuid,
            prevlabrecordsRetrofitCallback
        )
    }


    interface ChiefrevClickedListener {
        fun sendPrevtoChild(
            responseContent: ArrayList<PrevChiefComplaintResponseContent?>?
        )
    }

    fun setOnTextClickedListener(callback: ChiefrevClickedListener) {
        this.mainCallback = callback
    }

    //track Prev ChiefComplaints Visit
    fun trackPrevChiefAnalyticsVisit(type: String) {
        AnalyticsManager.getAnalyticsManager().trackPrevChiefComplaintsVisit(type)
    }

    //defining Interface
    interface CancelListener {
        fun cancelfunction(

        )
    }

    fun setOnTextClickedListener(callback: CancelListener) {
        this.mCallback = callback
    }

    fun refreshList(patientID: Int?, facilityid: Int?) {
        viewModel?.getPrevChiefComplaintList(
            patientID,
            facilityid,
            appPreferences?.getInt(AppConstants.ENCOUNTER_UUID),
            prevlabrecordsRetrofitCallback
        )
    }
}