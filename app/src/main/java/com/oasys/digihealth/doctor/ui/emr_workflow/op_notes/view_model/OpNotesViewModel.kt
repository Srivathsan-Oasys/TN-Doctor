package com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.view_model

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
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.add_consultations.OpNotesAddConsultationsReq
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.add_consultations.OpNotesAddConsultationsResp
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.get_default.GetOpNotesDefaultResp
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.prev_records.GetOpNotesPreviousRecordsResp
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.prev_records.observed_values.GetOpNotesObservedValuesResp
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.set_default.SetOpNotesDefaultReq
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.set_default.SetOpNotesDefaultResp
import com.oasys.digihealth.doctor.utils.Utils

class OpNotesViewModel(
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
        getAllProfileTypesRespRetrofitCallback: RetrofitCallback<GetOpNotesAllProfileTypesResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getOpNotesAllProfileTypes(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            profileType,
            departmentId
        ).enqueue(RetrofitMainCallback(getAllProfileTypesRespRetrofitCallback))
    }

    fun getOpNotes(
        facility_uuid: Int,
        profileUuid: Int,
        getOpNotesRestRetrofitCallback: RetrofitCallback<GetOpNotesDetailResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getOpNotes(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            profileUuid
        ).enqueue(RetrofitMainCallback(getOpNotesRestRetrofitCallback))
    }

    fun getEncounter(
        facility_uuid: Int,
        patientId: Int,
        departmentId: Int,
        encounterType: Int,
        getOpNotesEncounterByDocAndPatientIdRespCallback: RetrofitCallback<GetOpNotesEncounterByDocAndPatientIdResp>
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

        apiService?.getOpNotesEncounter(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            patientId,
            doctorId!!,
            departmentId,
            encounterType
        ).enqueue(RetrofitMainCallback(getOpNotesEncounterByDocAndPatientIdRespCallback))
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
        saveOpNotesDetailsReq: SaveOpNotesDetailsReq,
        saveOpNotesDetailsRespCallback: RetrofitCallback<SaveOpNotesDetailsResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.saveOpNotesAnswers(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            saveOpNotesDetailsReq
        ).enqueue(RetrofitMainCallback(saveOpNotesDetailsRespCallback))
    }

    fun getDefault(
        facility_uuid: Int,
        profile_type_uuid: Int,
        getOpNotesDefaultRespCallback: RetrofitCallback<GetOpNotesDefaultResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getOpNotesDefault(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            profile_type_uuid
        ).enqueue(RetrofitMainCallback(getOpNotesDefaultRespCallback))
    }

    fun setDefault(
        facility_uuid: Int,
        setOpNotesDefaultReq: SetOpNotesDefaultReq,
        setOpNotesDefaultRespCallback: RetrofitCallback<SetOpNotesDefaultResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.setOpNotesDefault(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            setOpNotesDefaultReq
        ).enqueue(RetrofitMainCallback(setOpNotesDefaultRespCallback))
    }

    fun getPreviousRecords(
        facility_uuid: Int,
        patientId: Int,
        profileType: Int,
        getOpNotesPreviousRecordsRespCallback: RetrofitCallback<GetOpNotesPreviousRecordsResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getOpNotesPreviousRecords(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            patientId,
            profileType
        ).enqueue(RetrofitMainCallback(getOpNotesPreviousRecordsRespCallback))
    }

    fun getObservedValues(
        facility_uuid: Int,
        patientId: Int,
        getOpNotesObservedValuesRespCallback: RetrofitCallback<GetOpNotesObservedValuesResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getOpNotesObservedValues(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            patientId
        ).enqueue(RetrofitMainCallback(getOpNotesObservedValuesRespCallback))
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
        opNotesAddConsultationsReq: OpNotesAddConsultationsReq,
        opNotesAddConsultationsRespCallback: RetrofitCallback<OpNotesAddConsultationsResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.opNotesAddConsultations(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            opNotesAddConsultationsReq
        ).enqueue(RetrofitMainCallback(opNotesAddConsultationsRespCallback))
    }
}