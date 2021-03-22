package com.oasys.digihealth.doctor.ui.emr_workflow.progress_notes.view

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentProgressNotesChildBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.progress_notes.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.progress_notes.view_model.ProgressNotesViewModel
import com.oasys.digihealth.doctor.utils.custom_views.CustomProgressDialog
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response

class ProgressNotesChildFragment : Fragment() {

    private var viewModel: ProgressNotesViewModel? = null
    private var binding: FragmentProgressNotesChildBinding? = null
    private var utils: Utils? = null
    private var customProgressDialog: CustomProgressDialog? = null

    private var appPreferences: AppPreferences? = null
    private var facilityId: Int? = null
    private var departmentUuid: Int? = null
    private var encounterType: Int? = null

    private var patientUuid: Int = 0
    private var encounter_uuid: Int = 0
    private var doctor_en_uuid: Int = 0

    private val progressNotesList = ArrayList<ResponseContent>()
    private var progressNotesAdapter: ProgressNotesAdapter? = null

    private var note: ResponseContent? = null

    private var currentNote = ""

    private val ADD = 0
    private val UPDATE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_progress_notes_child,
            container,
            false
        )
        utils = Utils(requireActivity())
        customProgressDialog = CustomProgressDialog(activity)

        viewModel = ViewModelProvider(this)[ProgressNotesViewModel::class.java]

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilityId = appPreferences?.getInt(AppConstants.FACILITY_UUID)
//        patientUuid = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        patientUuid = appPreferences?.getInt(AppConstants.PATIENT_UUID) ?: 0
        departmentUuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
//        encounterUuid = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)
//        encounterDoctorUuid = appPreferences?.getInt(AppConstants.ENCOUNTER_DOCTOR_UUID)

        trackProgressNotesVisit(utils?.getEncounterType())

        initViews()
        listeners()
        attachObservables()

        getProgressNotes()

        return binding?.root
    }

    private fun initViews() {
        binding?.recyclerViewProgressNotes?.layoutManager = LinearLayoutManager(activity)
        progressNotesAdapter = ProgressNotesAdapter(
            list = progressNotesList,
            editNote = { note ->
                currentNote = note.dName ?: ""
                binding?.etDailyNotes?.setText(note.pDailyNote)
                binding?.etSpecialNotes?.setText(note.pSpecialNote)
                changeSaveBtn(UPDATE)
                this.note = note
                editProgressNotes()
            },
            deleteNote = { note ->
                currentNote = note.dName ?: ""
                utils?.showDialog(
                    context = requireContext(),
                    title = "Delete",
                    msg = "Are you sure to delete \"${note.dName}\" record?",
                    positiveButtonText = getString(android.R.string.ok),
                    negativeButtonText = getString(android.R.string.cancel),
                    positiveButtonListener = { dialog, which ->
                        this.note = note
                        deleteProgressNotes()
                    },
                    negativeButtonListener = { dialog, which ->
                        dialog.dismiss()
                    }
                )
            })
        binding?.recyclerViewProgressNotes?.adapter = progressNotesAdapter
    }

    private fun listeners() {
        binding?.saveCardView?.setOnClickListener {
            if (binding?.tvSave?.text == getString(R.string.add)) {
                if (binding?.etDailyNotes?.text?.isNotEmpty() == true && binding?.etSpecialNotes?.text?.isNotEmpty() == true) {
                    trackProgressNotesAdd(utils?.getEncounterType(), "daily/special")
                } else if (binding?.etDailyNotes?.text?.isNotEmpty() == true) {
                    trackProgressNotesAdd(utils?.getEncounterType(), "daily")
                } else if (binding?.etSpecialNotes?.text?.isNotEmpty() == true) {
                    trackProgressNotesAdd(utils?.getEncounterType(), "special")
                }
            }
            getEncounter()
        }

        binding?.clearCardView?.setOnClickListener {
            clearAll()
        }
    }

    private fun attachObservables() {
        viewModel?.progress?.observe(requireActivity(), Observer { progress ->
            if (progress == View.VISIBLE) {
                customProgressDialog?.show()
            } else if (progress == View.GONE) {
                customProgressDialog?.dismiss()
            }
        })
    }

    private fun clearAll() {
        currentNote = ""
        binding?.etDailyNotes?.text?.clear()
        binding?.etSpecialNotes?.text?.clear()
    }

    private fun changeSaveBtn(type: Int) {
        when (type) {
            ADD -> {
                binding?.tvSave?.text = getString(R.string.add)
                binding?.tvSave?.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_save_white,
                    0,
                    0,
                    0
                )
            }
            UPDATE -> {
                binding?.tvSave?.text = getString(R.string.update)
                binding?.tvSave?.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_plus,
                    0,
                    0,
                    0
                )
            }
        }
    }

    private fun getProgressNotes() {
        viewModel?.getProgressNotes(facilityId!!, patientUuid, getProgressNotesRetrofitCallback)
    }

    private fun getEncounter() {
        viewModel?.getEncounter(
            facilityId!!,
            patientUuid,
            departmentUuid!!,
            encounterType!!,
            getEncounterByDocAndPatientIdRespCallback
        )
    }

    private fun createProgressNotes(
        capturedBy: String?,
        capturedOn: String?,
        isPhr: Boolean?,
        noteStatusUuid: Int?,
        progressNoteTypeUuid: Int?
    ) {
        val body = CreateProgressNotesReq()
        var item: CreateProgressNotesReqItem

        if (binding?.etDailyNotes?.text?.isNotEmpty() == true) {
            item = CreateProgressNotesReqItem(
                captured_by = capturedBy,
                patient_uuid = patientUuid,
                captured_on = capturedOn,
                daily_note = binding?.etDailyNotes?.text.toString(),
                department_uuid = departmentUuid?.toString(),
                encounter_type_uuid = encounterType,
                encounter_uuid = encounter_uuid,
                facility_uuid = facilityId?.toString(),
                is_phr = isPhr,
                note_status_uuid = noteStatusUuid,
                progressnote_type_uuid = progressNoteTypeUuid,
                special_note = ""
            )
            body.add(item)
        }
        if (binding?.etSpecialNotes?.text?.isNotEmpty() == true) {
            item = CreateProgressNotesReqItem(
                captured_by = capturedBy,
                patient_uuid = patientUuid,
                captured_on = capturedOn,
                daily_note = "",
                department_uuid = departmentUuid?.toString(),
                encounter_type_uuid = encounterType,
                encounter_uuid = encounter_uuid,
                facility_uuid = facilityId?.toString(),
                is_phr = isPhr,
                note_status_uuid = noteStatusUuid,
                progressnote_type_uuid = progressNoteTypeUuid,
                special_note = binding?.etSpecialNotes?.text?.toString()
            )
            body.add(item)
        }

        viewModel?.createProgressNotes(facilityId!!, body, createProgressNotesRespCallback)
    }

    private fun editProgressNotes() {
        viewModel?.editProgressNotes(facilityId!!, note?.pUuid!!, editProgressNotesRespRespCallback)
    }

    private fun updateProgressNotes(
        capturedBy: String?,
        capturedOn: String?,
        isPhr: Boolean?,
        noteStatusUuid: Int?,
        progressNoteTypeUuid: Int?
    ) {
        val body = UpdateProgressNotesReq(
            capturedBy = capturedBy,
            capturedOn = capturedOn,
            isPhr = isPhr,
            noteStatusUuid = noteStatusUuid,
            specialNote = binding?.etSpecialNotes?.text.toString(),
            progressnoteTypeUuid = progressNoteTypeUuid,
            facilityUuid = facilityId?.toString(),
            encounterUuid = encounter_uuid,
            encounterTypeUuid = encounterType,
            departmentUuid = departmentUuid?.toString(),
            dailyNote = binding?.etDailyNotes?.text.toString(),
            patientUuid = patientUuid.toString()
        )
        viewModel?.updateProgressNotes(
            facilityId!!,
            note?.pUuid!!,
            body,
            updateProgressNotesRespCallback
        )
    }

    private fun deleteProgressNotes() {
        viewModel?.deleteProgressNotes(facilityId!!, note?.pUuid!!, deleteProgressNotesRespCallback)
    }

    private val getProgressNotesRetrofitCallback = object : RetrofitCallback<GetProgressNotesResp> {
        override fun onSuccessfulResponse(responseBody: Response<GetProgressNotesResp>?) {
            if (responseBody?.isSuccessful == true) {
                responseBody.body()?.let { getProgressNotesResp ->
                    progressNotesList.clear()
                    getProgressNotesResp.responseContents?.let { progressNotesList.addAll(it) }
                    progressNotesAdapter?.notifyDataSetChanged()
                }
            }
        }

        override fun onBadRequest(errorBody: Response<GetProgressNotesResp>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
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

        override fun onFailure(s: String?) {
            if (s != null) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    private val getEncounterByDocAndPatientIdRespCallback =
        object : RetrofitCallback<GetEncounterByDocAndPatientIdResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetEncounterByDocAndPatientIdResp>?) {
                val encounterResponseContent = responseBody?.body()?.responseContents!!
                doctor_en_uuid = encounterResponseContent.get(0).encounterDoctors?.get(0)?.uuid ?: 0
                encounter_uuid = encounterResponseContent.get(0).uuid ?: 0
                patientUuid = encounterResponseContent.get(0).patientUuid ?: 0
                appPreferences?.saveInt(AppConstants.ENCOUNTER_DOCTOR_UUID, doctor_en_uuid)
                appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_uuid)

                if (responseBody.body()?.responseContents?.isNotEmpty()!!) {
                    responseBody.body()?.let { getEncounterByDocAndPatientIdResp ->
                        if (getEncounterByDocAndPatientIdResp.responseContents?.isNotEmpty() == true) {
                            val userDetailsRoomRepository =
                                UserDetailsRoomRepository(context?.applicationContext as Application)
                            val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
                            val doctorId = userDataStoreBean?.uuid
                            if (binding?.tvSave?.text == getString(R.string.add)) {
                                getEncounterByDocAndPatientIdResp.responseContents?.get(0)
                                    .let { responseContentX ->
                                        createProgressNotes(
                                            capturedBy = doctorId?.toString(),
                                            capturedOn = utils?.getCurrentDateTime("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
                                            isPhr = true,
                                            noteStatusUuid = 1,
                                            progressNoteTypeUuid = 0
                                        )
                                    }
                            } else if (binding?.tvSave?.text == getString(R.string.update)) {
                                getEncounterByDocAndPatientIdResp.responseContents?.get(0)
                                    .let { responseContentX ->
                                        updateProgressNotes(
                                            capturedBy = doctorId?.toString(),
                                            capturedOn = utils?.getCurrentDateTime("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
                                            isPhr = true,
                                            noteStatusUuid = 1,
                                            progressNoteTypeUuid = 0
                                        )
                                    }
                            }
                        }
                    }
                } else {
                    viewModel?.createEncounter(
                        patientUuid,
                        encounterType!!,
                        createEncounterRetrofitCallback
                    )
                }
            }

            override fun onBadRequest(errorBody: Response<GetEncounterByDocAndPatientIdResp>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
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

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private val createEncounterRetrofitCallback =
        object : RetrofitCallback<CreateEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<CreateEncounterResponseModel>) {


                doctor_en_uuid = response.body()?.responseContents?.encounterDoctor?.uuid!!.toInt()
                encounter_uuid = response.body()?.responseContents?.encounter?.uuid!!.toInt()
                patientUuid =
                    response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()

                val userDetailsRoomRepository =
                    UserDetailsRoomRepository(context?.applicationContext as Application)
                val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
                val doctorId = userDataStoreBean?.uuid
                if (binding?.tvSave?.text == getString(R.string.add)) {

                    createProgressNotes(
                        capturedBy = doctorId?.toString(),
                        capturedOn = utils?.getCurrentDateTime("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
                        isPhr = true,
                        noteStatusUuid = 1,
                        progressNoteTypeUuid = 0
                    )

                } else if (binding?.tvSave?.text == getString(R.string.update)) {
                    updateProgressNotes(
                        capturedBy = doctorId?.toString(),
                        capturedOn = utils?.getCurrentDateTime("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
                        isPhr = true,
                        noteStatusUuid = 1,
                        progressNoteTypeUuid = 0
                    )
                }


            }

            override fun onBadRequest(response: Response<CreateEncounterResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: CreateEncounterResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        CreateEncounterResponseModel::class.java
                    )
                    /*   utils?.showToast(
                           R.color.negativeToast,
                           binding?.mainLayout!!,
                           responseModel.message!!
                       )*/
                } catch (e: Exception) {
                    /*   utils?.showToast(
                           R.color.negativeToast,
                           binding?.mainLayout!!,
                           getString(R.string.something_went_wrong)
                       )*/
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                /* utils?.showToast(
                     R.color.negativeToast,
                     binding?.mainLayout!!,
                     getString(R.string.something_went_wrong)
                 )*/
            }

            override fun onUnAuthorized() {
                /*    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.unauthorized)
                    )*/
            }

            override fun onForbidden() {
                /*   utils?.showToast(
                       R.color.negativeToast,
                       binding?.mainLayout!!,
                       getString(R.string.something_went_wrong)
                   )*/
            }

            override fun onFailure(failure: String) {
//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private val createProgressNotesRespCallback =
        object : RetrofitCallback<CreateProgressNotesResp> {
            override fun onSuccessfulResponse(responseBody: Response<CreateProgressNotesResp>?) {
                if (responseBody?.isSuccessful == true) {
                    Toast.makeText(
                        requireContext(),
                        "Progress Notes Added Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    getProgressNotes()
                    clearAll()
                }
            }

            override fun onBadRequest(errorBody: Response<CreateProgressNotesResp>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
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

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private val editProgressNotesRespRespCallback =
        object : RetrofitCallback<EditProgressNotesResp> {
            override fun onSuccessfulResponse(responseBody: Response<EditProgressNotesResp>?) {
                if (responseBody?.isSuccessful == true) {

                }
            }

            override fun onBadRequest(errorBody: Response<EditProgressNotesResp>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
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

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private val updateProgressNotesRespCallback =
        object : RetrofitCallback<UpdateProgressNotesResp> {
            override fun onSuccessfulResponse(responseBody: Response<UpdateProgressNotesResp>?) {
                if (responseBody?.isSuccessful == true) {
                    Toast.makeText(
                        requireContext(),
                        "$currentNote updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    changeSaveBtn(ADD)
                    getProgressNotes()
                    clearAll()
                }
            }

            override fun onBadRequest(errorBody: Response<UpdateProgressNotesResp>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
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

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private val deleteProgressNotesRespCallback =
        object : RetrofitCallback<DeleteProgressNotesResp> {
            override fun onSuccessfulResponse(responseBody: Response<DeleteProgressNotesResp>?) {
                if (responseBody?.isSuccessful == true) {
                    Toast.makeText(
                        requireContext(),
                        "$currentNote deleted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    getProgressNotes()
                }
            }

            override fun onBadRequest(errorBody: Response<DeleteProgressNotesResp>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
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

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private fun trackProgressNotesVisit(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackProgressNotesVisit(type)
    }

    private fun trackProgressNotesAdd(type: String?, pntype: String?) {
        AnalyticsManager.getAnalyticsManager().trackProgressNotesAdd(type, pntype)
    }
}
