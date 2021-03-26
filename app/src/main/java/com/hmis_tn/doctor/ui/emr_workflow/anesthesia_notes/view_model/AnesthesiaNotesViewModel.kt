package com.hmis_tn.doctor.ui.emr_workflow.anesthesia_notes.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitMainCallback
import com.hmis_tn.doctor.ui.emr_workflow.anesthesia_notes.model.*
import com.hmis_tn.doctor.ui.emr_workflow.anesthesia_notes.model.add_consultations.AnaesthesiaNotesAddConsultationsReq
import com.hmis_tn.doctor.ui.emr_workflow.anesthesia_notes.model.add_consultations.AnaesthesiaNotesAddConsultationsResp
import com.hmis_tn.doctor.ui.emr_workflow.anesthesia_notes.model.get_default.GetAnesthesiaNotesDefaultResp
import com.hmis_tn.doctor.ui.emr_workflow.anesthesia_notes.model.prev_records.GetAnesthesiaNotesPreviousRecordsResp
import com.hmis_tn.doctor.ui.emr_workflow.anesthesia_notes.model.prev_records.observed_values.GetAnesthesiaNotesObservedValuesResp
import com.hmis_tn.doctor.ui.emr_workflow.anesthesia_notes.model.set_default.SetAnesthesiaNotesDefaultReq
import com.hmis_tn.doctor.ui.emr_workflow.anesthesia_notes.model.set_default.SetAnesthesiaNotesDefaultResp
import com.hmis_tn.doctor.ui.emr_workflow.model.EmrWorkFlowResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.CreateEncounterRequestModel
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.Encounter
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.EncounterDoctor
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.hmis_tn.doctor.utils.Utils

class AnesthesiaNotesViewModel(
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
        getAllProfileTypesRespRetrofitCallback: RetrofitCallback<GetAnesthesiaNotesAllProfileTypesResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getAnesthesiaNotesAllProfileTypes(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            profileType,
            departmentId
        )?.enqueue(RetrofitMainCallback(getAllProfileTypesRespRetrofitCallback))
    }

    fun getAnesthesiaNotes(
        facility_uuid: Int,
        profileUuid: Int,
        getAnesthesiaNotesRestRetrofitCallback: RetrofitCallback<GetAnesthesiaNotesDetailResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getAnesthesiaNotes(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            profileUuid
        )?.enqueue(RetrofitMainCallback(getAnesthesiaNotesRestRetrofitCallback))
    }

    fun getEncounter(
        facility_uuid: Int,
        patientId: Int,
        departmentId: Int,
        encounterType: Int,
        getAnesthesiaNotesEncounterByDocAndPatientIdRespCallback: RetrofitCallback<GetAnesthesiaNotesEncounterByDocAndPatientIdResp>
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

        apiService?.getAnesthesiaNotesEncounter(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            patientId,
            doctorId!!,
            departmentId,
            encounterType
        )?.enqueue(RetrofitMainCallback(getAnesthesiaNotesEncounterByDocAndPatientIdRespCallback))
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
        saveAnesthesiaNotesDetailsReq: SaveAnesthesiaNotesDetailsReq,
        saveAnesthesiaNotesDetailsRespCallback: RetrofitCallback<SaveAnesthesiaNotesDetailsResp>
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

        apiService?.saveAnesthesiaNotesAnswers(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            saveAnesthesiaNotesDetailsReq
        )?.enqueue(RetrofitMainCallback(saveAnesthesiaNotesDetailsRespCallback))
    }

    fun getDefault(
        facility_uuid: Int,
        profile_type_uuid: Int,
        getAnesthesiaNotesDefaultRespCallback: RetrofitCallback<GetAnesthesiaNotesDefaultResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getAnesthesiaNotesDefault(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            profile_type_uuid
        )?.enqueue(RetrofitMainCallback(getAnesthesiaNotesDefaultRespCallback))
    }

    fun setDefault(
        facility_uuid: Int,
        setAnesthesiaNotesDefaultReq: SetAnesthesiaNotesDefaultReq,
        setAnesthesiaNotesDefaultRespCallback: RetrofitCallback<SetAnesthesiaNotesDefaultResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.setAnesthesiaNotesDefault(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            setAnesthesiaNotesDefaultReq
        )?.enqueue(RetrofitMainCallback(setAnesthesiaNotesDefaultRespCallback))
    }

    fun getPreviousRecords(
        facility_uuid: Int,
        patientId: Int,
        profileType: Int,
        getAnesthesiaNotesPreviousRecordsRespCallback: RetrofitCallback<GetAnesthesiaNotesPreviousRecordsResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getAnesthesiaNotesPreviousRecords(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            patientId,
            profileType
        )?.enqueue(RetrofitMainCallback(getAnesthesiaNotesPreviousRecordsRespCallback))
    }

    fun getObservedValues(
        facility_uuid: Int,
        patientId: Int,
        getAnesthesiaNotesObservedValuesRespCallback: RetrofitCallback<GetAnesthesiaNotesObservedValuesResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getAnesthesiaNotesObservedValues(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            patientId
        )?.enqueue(RetrofitMainCallback(getAnesthesiaNotesObservedValuesRespCallback))
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
        )?.enqueue(RetrofitMainCallback(emrWorkFlowResponseModelCallback))
    }

    fun addConsultations(
        facility_uuid: Int,
        anaesthesiaNotesAddConsultationsReq: AnaesthesiaNotesAddConsultationsReq,
        anaesthesiaNotesAddConsultationsRespCallback: RetrofitCallback<AnaesthesiaNotesAddConsultationsResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.anaesthesiaNotesAddConsultations(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            anaesthesiaNotesAddConsultationsReq
        )?.enqueue(RetrofitMainCallback(anaesthesiaNotesAddConsultationsRespCallback))
    }
}