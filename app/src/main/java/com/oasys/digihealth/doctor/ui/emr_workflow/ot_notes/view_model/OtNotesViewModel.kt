package com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.application.HmisApplication
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EmrWorkFlowResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.CreateEncounterRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.Encounter
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.EncounterDoctor
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.add_consultations.OtNotesAddConsultationsReq
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.add_consultations.OtNotesAddConsultationsResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.get_default.GetOtNotesDefaultResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.prev_records.GetOtNotesPreviousRecordsResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.prev_records.observed_values.GetOtNotesObservedValuesResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.set_default.SetOtNotesDefaultReq
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.set_default.SetOtNotesDefaultResp
import com.oasys.digihealth.doctor.utils.Utils

class OtNotesViewModel(
    application: Application
) : AndroidViewModel(
    application
) {
    var errorText = MutableLiveData<String>()

    var progress = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var department_UUID: Int? = 0
    private var facility_id: Int? = 0

    var appPreferences: AppPreferences? = null

    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)

    }

    fun getAllProfileTypes(
        facility_uuid: Int,
        profileType: Int,
        departmentId: Int,
        getAllProfileTypesRespRetrofitCallback: RetrofitCallback<GetOtNotesAllProfileTypesResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getOtNotesAllProfileTypes(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            profileType,
            departmentId
        ).enqueue(RetrofitMainCallback(getAllProfileTypesRespRetrofitCallback))
    }

    fun getOtNotes(
        facility_uuid: Int,
        profileUuid: Int,
        getOtNotesRestRetrofitCallback: RetrofitCallback<GetOtNotesDetailResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getOtNotes(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            profileUuid
        ).enqueue(RetrofitMainCallback(getOtNotesRestRetrofitCallback))
    }

    fun getEncounter(
        facility_uuid: Int,
        patientId: Int,
        departmentId: Int,
        encounterType: Int,
        getOtNotesEncounterByDocAndPatientIdRespCallback: RetrofitCallback<GetOtNotesEncounterByDocAndPatientIdResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val doctorId = userDataStoreBean?.uuid

        apiService?.getOtNotesEncounter(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            patientId,
            doctorId!!,
            departmentId,
            encounterType
        ).enqueue(RetrofitMainCallback(getOtNotesEncounterByDocAndPatientIdRespCallback))
    }

    fun createEncounter(
        patientUuid: Int,
        encounterType: Int,
        createEncounterRetrofitCallback: RetrofitCallback<CreateEncounterResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        val createEncounterRequestModel = CreateEncounterRequestModel()

        val encounter = Encounter()
        encounter.admission_request_uuid = 0
        encounter.admission_uuid = 0
        encounter.appointment_uuid = 0
        encounter.department_uuid = appPreferences?.getInt(
            AppConstants.DEPARTMENT_UUID
        )
        encounter.discharge_type_uuid = 0
        encounter.encounter_identifier = 0
        encounter.encounter_priority_uuid = 0
        encounter.encounter_status_uuid = 0
        encounter.encounter_type_uuid = encounterType
        encounter.facility_uuid = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        encounter.patient_uuid = patientUuid

        createEncounterRequestModel.encounter = encounter

        val encounterDoctor = EncounterDoctor()
        encounterDoctor.department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        encounterDoctor.dept_visit_type_uuid = encounterType
        encounterDoctor.doctor_uuid = userDataStoreBean?.uuid
        encounterDoctor.doctor_visit_type_uuid = encounterType
        encounterDoctor.patient_uuid = patientUuid
        encounterDoctor.session_type_uuid = 0
        encounterDoctor.speciality_uuid = 0
        encounterDoctor.sub_deparment_uuid = 0
        encounterDoctor.visit_type_uuid = encounterType

        createEncounterRequestModel.encounterDoctor = encounterDoctor

        apiService?.createEncounter(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            createEncounterRequestModel

        )!!.enqueue(
            RetrofitMainCallback(createEncounterRetrofitCallback)
        )
    }

    fun saveAnswers(
        facility_uuid: Int,
        saveOtNotesDetailsReq: SaveOtNotesDetailsReq,
        saveOtNotesDetailsRespCallback: RetrofitCallback<SaveOtNotesDetailsResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val doctorId = userDataStoreBean?.uuid

        apiService?.saveOtNotesAnswers(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            saveOtNotesDetailsReq
        ).enqueue(RetrofitMainCallback(saveOtNotesDetailsRespCallback))
    }

    fun getDefault(
        facility_uuid: Int,
        profile_type_uuid: Int,
        getOtNotesDefaultRespCallback: RetrofitCallback<GetOtNotesDefaultResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getOtNotesDefault(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            profile_type_uuid
        ).enqueue(RetrofitMainCallback(getOtNotesDefaultRespCallback))
    }

    fun setDefault(
        facility_uuid: Int,
        setOtNotesDefaultReq: SetOtNotesDefaultReq,
        setOtNotesDefaultRespCallback: RetrofitCallback<SetOtNotesDefaultResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.setOtNotesDefault(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            setOtNotesDefaultReq
        ).enqueue(RetrofitMainCallback(setOtNotesDefaultRespCallback))
    }

    fun getPreviousRecords(
        facility_uuid: Int,
        patientId: Int,
        profileType: Int,
        getOtNotesPreviousRecordsRespCallback: RetrofitCallback<GetOtNotesPreviousRecordsResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getOtNotesPreviousRecords(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            patientId,
            profileType
        ).enqueue(RetrofitMainCallback(getOtNotesPreviousRecordsRespCallback))
    }

    fun getObservedValues(
        facility_uuid: Int,
        patientId: Int,
        getOtNotesObservedValuesRespCallback: RetrofitCallback<GetOtNotesObservedValuesResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getOtNotesObservedValues(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            patientId
        ).enqueue(RetrofitMainCallback(getOtNotesObservedValuesRespCallback))
    }

    fun getEmrWorkflow(
        contextId: Int,
        emrWorkFlowResponseModelCallback: RetrofitCallback<EmrWorkFlowResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getEmrWorkflowForIpAndOp(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            contextId
        ).enqueue(RetrofitMainCallback(emrWorkFlowResponseModelCallback))
    }

    fun addConsultations(
        facility_uuid: Int,
        otNotesAddConsultationsReq: OtNotesAddConsultationsReq,
        otNotesAddConsultationsRespCallback: RetrofitCallback<OtNotesAddConsultationsResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.otNotesAddConsultations(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            otNotesAddConsultationsReq
        ).enqueue(RetrofitMainCallback(otNotesAddConsultationsRespCallback))
    }
}