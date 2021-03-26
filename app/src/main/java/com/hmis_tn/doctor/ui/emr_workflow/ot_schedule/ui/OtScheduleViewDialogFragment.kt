package com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.ui

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogOtscheduleViewFragmentBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.fire_base_analytics.AnalyticsManager
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.delete.DeleteOtScheduleReq
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.delete.DeleteOtScheduleResp
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.response.OtScheduleToCalendarresponseContent
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.view.ViewOtScheduleReq
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.view.ViewOtScheduleResp
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.viewmodel.AddSurgeryViewModel
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.viewmodel.AddSurgeryViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response

class OtScheduleViewDialogFragment(
    private val otScheduleToCalendarresponseContent: OtScheduleToCalendarresponseContent?
) : DialogFragment() {

    private var viewModel: AddSurgeryViewModel? = null
    var binding: DialogOtscheduleViewFragmentBinding? = null
    private var appPreferences: AppPreferences? = null
    private var facilityId: Int? = null
    private var departId: Int? = null
    private var doctorId: Int? = null
    private var patientId: Int? = null
    private var utils: Utils? = null
    var callbacksurgury: OnRefreshListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_otschedule_view_fragment,
                container,
                false
            )
        viewModel = AddSurgeryViewModelFactory(
            requireActivity().application
        )
            .create(AddSurgeryViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils = Utils(requireContext())
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilityId = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        departId = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        doctorId = appPreferences?.getInt(AppConstants.ENCOUNTER_DOCTOR_UUID)
        patientId = appPreferences?.getInt(AppConstants.PATIENT_UUID)


        initViews()
        listeners()

        return binding?.root
    }

    private fun initViews() {
//        viewOtSchedule(otScheduleToCalendarresponseContent?.os_uuid.toString())

        populateAnswers()
    }

    private fun listeners() {
        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.cancelCardView?.setOnClickListener {
            dialog?.cancel()
        }

        binding?.editCardView?.setOnClickListener {
            val ft = childFragmentManager.beginTransaction()
            val surgeryDialog = AddSurgeryDialogFragment(true, otScheduleToCalendarresponseContent)
            surgeryDialog.show(ft, "Tag")
        }

        binding?.deleteCardView?.setOnClickListener {
            val userDetailsRoomRepository =
                UserDetailsRoomRepository(context?.applicationContext as Application)
            val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
            val doctorId = userDataStoreBean?.uuid

            if (
                doctorId == otScheduleToCalendarresponseContent?.os_uuid &&
                patientId?.toString() == otScheduleToCalendarresponseContent?.patient_uhid
            ) {
                trackOtScheduleEdit(utils?.getEncounterType())
                deleteSurgery(otScheduleToCalendarresponseContent?.os_uuid ?: 0)
            } else {
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, "Not Accessible")
            }
        }
    }

    private fun populateAnswers() {
        binding?.pinSearch?.setText(otScheduleToCalendarresponseContent?.patient_uhid ?: "")

        var gender = ""
        if (otScheduleToCalendarresponseContent?.patient_gender_uuid != null && otScheduleToCalendarresponseContent.patient_gender_uuid != 0) {
            if (otScheduleToCalendarresponseContent.patient_gender_uuid == 1) {
                gender = "Male"
            } else if (otScheduleToCalendarresponseContent.patient_gender_uuid == 2) {
                gender = "Female"
            } else if (otScheduleToCalendarresponseContent.patient_gender_uuid == 3) {
                gender = "Transgender"
            } else {
                gender = "Male"
            }

            binding?.detailsTextView?.text =
                "${otScheduleToCalendarresponseContent.title_name ?: ""} " +
                        "${otScheduleToCalendarresponseContent.patient_first_name ?: ""} " +
                        "${otScheduleToCalendarresponseContent.patient_last_name ?: ""} / " +
                        "${otScheduleToCalendarresponseContent.patient_age ?: "0"} Year(s) / " +
                        gender + " / " +
                        "${otScheduleToCalendarresponseContent.patient_mobile ?: ""}"
        }

        binding?.surgeryNameSearch?.setText(
            otScheduleToCalendarresponseContent?.procedures_name ?: ""
        )
        binding?.fromDate?.setText(otScheduleToCalendarresponseContent?.os_start_date ?: "")
        binding?.toDate?.setText(otScheduleToCalendarresponseContent?.os_end_date ?: "")
        binding?.spinnerLession?.setText(otScheduleToCalendarresponseContent?.lesion_name ?: "")
        binding?.spinnerSide?.setText(otScheduleToCalendarresponseContent?.body_side_name ?: "")
        binding?.spinnerAnaes?.setText(
            otScheduleToCalendarresponseContent?.anesthesia_type_name ?: ""
        )
        binding?.spinnerPosition?.setText(otScheduleToCalendarresponseContent?.postions_name ?: "")
        binding?.llnEditText?.setText(otScheduleToCalendarresponseContent?.os_incision ?: "")
        binding?.approachEditText?.setText(otScheduleToCalendarresponseContent?.os_approach ?: "")
        binding?.spinnerOTtype?.setText(otScheduleToCalendarresponseContent?.ot_type_name ?: "")
        binding?.spinnerOTName?.setText(otScheduleToCalendarresponseContent?.ot_theatre_name ?: "")
        binding?.diagnosisSearch?.setText(otScheduleToCalendarresponseContent?.diagnosis_name ?: "")
        binding?.chiefSearch?.setText(
            "${otScheduleToCalendarresponseContent?.cheif_salutation_name ?: ""} ${otScheduleToCalendarresponseContent?.cheif_first_name ?: ""}"
        )
        binding?.surgenSearch?.setText(
            "${otScheduleToCalendarresponseContent?.surgeon_salutation_name ?: ""} ${otScheduleToCalendarresponseContent?.surgeon_first_name ?: ""}"
        )
        binding?.assistantSurgenSearch?.setText(
            "${otScheduleToCalendarresponseContent?.ass_surgeon_salutation_name ?: ""} ${otScheduleToCalendarresponseContent?.ass_surgeon_first_name ?: ""}"
        )
        binding?.assistantNurseSearch?.setText(
            "${otScheduleToCalendarresponseContent?.assistant_nurse_salutation_name ?: ""} ${otScheduleToCalendarresponseContent?.assistant_nurse_first_name ?: ""}"
        )
        binding?.spinnerAnth?.setText(
            "${otScheduleToCalendarresponseContent?.anaesthetist_salutation_name ?: ""} ${otScheduleToCalendarresponseContent?.anaesthetist_first_name ?: ""}"
        )

        if (otScheduleToCalendarresponseContent?.os_is_force_booking?.toString().equals("true")) {
            binding?.etForceBook?.setText("Yes")
        } else if (otScheduleToCalendarresponseContent?.os_is_force_booking?.toString()
                .equals("false")
        ) {
            binding?.etForceBook?.setText("No")
        }

        binding?.spinnerPriority?.setText(otScheduleToCalendarresponseContent?.priority_name ?: "")
        binding?.spinnerGrade?.setText(otScheduleToCalendarresponseContent?.grade_name ?: "")
        binding?.commentsSearch?.setText(otScheduleToCalendarresponseContent?.os_comments ?: "")
    }

    private fun viewOtSchedule(id: String?) {
        val body = ViewOtScheduleReq(
            Id = id
        )
        viewModel?.viewOtSchedule(facilityId, body, viewOtScheduleRespCallback)
    }

    private val viewOtScheduleRespCallback = object : RetrofitCallback<ViewOtScheduleResp> {
        override fun onSuccessfulResponse(responseBody: Response<ViewOtScheduleResp>?) {
            responseBody?.body()?.let { viewOtScheduleResp ->
                viewOtScheduleResp.responseContents?.let { responseContents ->

                }
            }
        }

        override fun onBadRequest(errorBody: Response<ViewOtScheduleResp>?) {
            val gson = GsonBuilder().create()
            val responseModel: ViewOtScheduleResp
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()?.string(),
                    ViewOtScheduleResp::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    ""
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

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel?.progressBar?.value = 8
        }
    }

    private fun deleteSurgery(id: Int) {
        val body = DeleteOtScheduleReq(id.toString())
        viewModel?.deleteOtSchedule(facilityId!!, body, deleteOtScheduleRespCallback)
    }

    private val deleteOtScheduleRespCallback = object : RetrofitCallback<DeleteOtScheduleResp> {
        override fun onSuccessfulResponse(responseBody: Response<DeleteOtScheduleResp>?) {
            if (responseBody?.isSuccessful == true) {
                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    "Record Deleted"
                )
                dialog?.dismiss()
            }
        }

        override fun onBadRequest(errorBody: Response<DeleteOtScheduleResp>?) {
            val gson = GsonBuilder().create()
            val responseModel: DeleteOtScheduleResp
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    DeleteOtScheduleResp::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    ""
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

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progressBar.value = 8
        }
    }

    fun setOnRefreshListener(callback: OnRefreshListener) {
        this.callbacksurgury = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    interface OnRefreshListener {
        fun onRefreshList()
    }

    private fun trackOtScheduleEdit(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackOtScheduleEdit(type)
    }
}