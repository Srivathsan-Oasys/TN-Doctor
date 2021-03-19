package com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.view_model

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
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.add_consultations.IpCaseSheetAddConsultationsReq
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.add_consultations.IpCaseSheetAddConsultationsResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.get_default.GetIpCaseSheetDefaultResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.prev_records.GetIpCaseSheetPreviousRecordsResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.prev_records.observed_values.GetIpCaseSheetObservedValuesResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.set_default.SetIpCaseSheetDefaultReq
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.set_default.SetIpCaseSheetDefaultResp
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EmrWorkFlowResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.CreateEncounterRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.Encounter
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.EncounterDoctor
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.utils.Utils

class IpCaseSheetViewModel(application: Application) : AndroidViewModel(application) {

    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository =
        UserDetailsRoomRepository(application)

    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
    }

    fun getAllProfileTypes(
        facility_uuid: Int,
        profileType: Int,
        departmentId: Int,
        getAllProfileTypesRespRetrofitCallback: RetrofitCallback<GetIpCaseSheetAllProfileTypesResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getIpCaseSheetAllProfileTypes(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            profileType,
            departmentId
        ).enqueue(RetrofitMainCallback(getAllProfileTypesRespRetrofitCallback))
    }

    fun getCaseSheet(
        facility_uuid: Int,
        profileUuid: Int,
        getCaseSheetRestRetrofitCallback: RetrofitCallback<GetCaseSheetDetailResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getCaseSheet(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            profileUuid
        ).enqueue(RetrofitMainCallback(getCaseSheetRestRetrofitCallback))
    }

    fun getEncounter(
        facility_uuid: Int,
        patientId: Int,
        departmentId: Int,
        encounterType: Int,
        getIpCaseSheetEncounterByDocAndPatientIdRespCallback: RetrofitCallback<GetIpCaseSheetEncounterByDocAndPatientIdResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val doctorId = userDataStoreBean?.uuid

        apiService?.getIpCaseSheetEncounter(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            patientId,
            doctorId!!,
            departmentId,
            encounterType
        ).enqueue(RetrofitMainCallback(getIpCaseSheetEncounterByDocAndPatientIdRespCallback))
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
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

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
        saveCaseSheetDetailsReq: SaveCaseSheetDetailsReq,
        saveCaseSheetDetailsRespCallback: RetrofitCallback<SaveCaseSheetDetailsResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val doctorId = userDataStoreBean?.uuid

        apiService?.saveCaseSheetAnswers(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            saveCaseSheetDetailsReq
        ).enqueue(RetrofitMainCallback(saveCaseSheetDetailsRespCallback))
    }

    fun getDefault(
        facility_uuid: Int,
        profile_type_uuid: Int,
        getIpCaseSheetDefaultRespCallback: RetrofitCallback<GetIpCaseSheetDefaultResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getIpCaseSheetDefault(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            profile_type_uuid
        ).enqueue(RetrofitMainCallback(getIpCaseSheetDefaultRespCallback))
    }

    fun setDefault(
        facility_uuid: Int,
        setIpCaseSheetDefaultReq: SetIpCaseSheetDefaultReq,
        setIpCaseSheetDefaultRespCallback: RetrofitCallback<SetIpCaseSheetDefaultResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.setIpCaseSheetDefault(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            setIpCaseSheetDefaultReq
        ).enqueue(RetrofitMainCallback(setIpCaseSheetDefaultRespCallback))
    }

    fun getPreviousRecords(
        facility_uuid: Int,
        patientId: Int,
        profileType: Int,
        getIpCaseSheetPreviousRecordsRespCallback: RetrofitCallback<GetIpCaseSheetPreviousRecordsResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getIpCaseSheetPreviousRecords(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            patientId,
            profileType
        ).enqueue(RetrofitMainCallback(getIpCaseSheetPreviousRecordsRespCallback))
    }

    fun getObservedValues(
        facility_uuid: Int,
        patientId: Int,
        getIpCaseSheetObservedValuesRespCallback: RetrofitCallback<GetIpCaseSheetObservedValuesResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getIpCaseSheetObservedValues(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            patientId
        ).enqueue(RetrofitMainCallback(getIpCaseSheetObservedValuesRespCallback))
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
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getEmrWorkflowForIpAndOp(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            contextId
        ).enqueue(RetrofitMainCallback(emrWorkFlowResponseModelCallback))
    }

    fun addConsultations(
        facility_uuid: Int,
        ipCaseSheetAddConsultationsReq: IpCaseSheetAddConsultationsReq,
        ipCaseSheetAddConsultationsRespCallback: RetrofitCallback<IpCaseSheetAddConsultationsResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.ipCaseSheetAddConsultations(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            ipCaseSheetAddConsultationsReq
        ).enqueue(RetrofitMainCallback(ipCaseSheetAddConsultationsRespCallback))
    }
}