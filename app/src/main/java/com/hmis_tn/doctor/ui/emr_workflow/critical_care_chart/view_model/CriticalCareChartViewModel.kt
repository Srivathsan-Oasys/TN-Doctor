package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.view_model

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
import com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.*
import com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.compare_data.GetCriticalCareChartCompareDataResp
import com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.config.CriticalCareChartFilterHeadingsResponse
import com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.headings.GetCriticalCareChartHeadingsReq
import com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.headings.GetCriticalCareChartHeadingsResp
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.CreateEncounterRequestModel
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.Encounter
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.EncounterDoctor
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.hmis_tn.doctor.utils.Utils

class CriticalCareChartViewModel(application: Application) : AndroidViewModel(application) {

    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
    }

    fun getCriticalCareChartHeadings(
        facility_uuid: Int,
        getCriticalCareChartHeadingsReq: GetCriticalCareChartHeadingsReq,
        getCriticalCareChartHeadingsRespCallback: RetrofitCallback<GetCriticalCareChartHeadingsResp>
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

        apiService?.getCriticalCareChartHeadings(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            getCriticalCareChartHeadingsReq
        )?.enqueue(RetrofitMainCallback(getCriticalCareChartHeadingsRespCallback))
    }

    fun getCriticalCareChartMaster(
        facility_uuid: Int,
        getCriticalCareChartMasterReq: GetCriticalCareChartMasterReq,
        getCriticalCareChartMasterRespCallback: RetrofitCallback<GetCriticalCareChartMasterResp>
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

        apiService?.getCriticalCareChartMaster(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            getCriticalCareChartMasterReq
        )?.enqueue(RetrofitMainCallback(getCriticalCareChartMasterRespCallback))
    }

    fun getCriticalCareChartByPatientId(
        facility_uuid: Int,
        patientUuid: Int,
        criticalCareType: Int,
        getCriticalCareChartByPatientIdResp: RetrofitCallback<GetCriticalCareChartByPatientIdResp>
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

        apiService?.getCriticalCareChartByPatientIdResp(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            patientUuid,
            criticalCareType
        )?.enqueue(RetrofitMainCallback(getCriticalCareChartByPatientIdResp))
    }

    fun getCriticalCareChartEncounter(
        facility_uuid: Int,
        patientUuid: Int,
        encounterType: Int,
        getCriticalCareChartEncounterRespCallback: RetrofitCallback<GetCriticalCareChartEncounterResp>
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
        val departmentId = userDataStoreBean?.department_uuid

        apiService?.getCriticalCareChartEncounter(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            patientUuid,
            doctorId!!,
            departmentId!!,
            encounterType
        )?.enqueue(RetrofitMainCallback(getCriticalCareChartEncounterRespCallback))
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

    fun postCriticalCareChartCreate(
        facility_uuid: Int,
        postCriticalCareChartCreateReq: PostCriticalCareChartCreateReq,
        postCriticalCareChartCreateRespCallback: RetrofitCallback<PostCriticalCareChartCreateResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.postCriticalCareChartCreate(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            postCriticalCareChartCreateReq
        )?.enqueue(RetrofitMainCallback(postCriticalCareChartCreateRespCallback))
    }

    fun postCriticalCareChartUpdate(
        facility_uuid: Int,
        postCriticalCareChartUpdateReq: PostCriticalCareChartUpdateReq,
        postCriticalCareChartUpdateRespCallback: RetrofitCallback<PostCriticalCareChartUpdateResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.postCriticalCareChartUpdate(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            postCriticalCareChartUpdateReq
        )?.enqueue(RetrofitMainCallback(postCriticalCareChartUpdateRespCallback))
    }

    fun getCriticalCareChartCompareData(
        facility_uuid: Int,
        patientUuid: Int,
        criticalCareType: Int,
        fromDate: String,
        toDate: String,
        getCriticalCareChartCompareDataRespCallback: RetrofitCallback<GetCriticalCareChartCompareDataResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getCriticalCareChartCompareData(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            patientUuid,
            criticalCareType,
            fromDate,
            toDate
        )?.enqueue(RetrofitMainCallback(getCriticalCareChartCompareDataRespCallback))
    }


    fun getCriticalCareChartFilterHeadings(
        facility_uuid: Int,
        getCriticalCareChartHeadingsRespCallback: RetrofitCallback<CriticalCareChartFilterHeadingsResponse>
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

        apiService?.getCriticalCareChartFilterHeadings(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            appPreferences!!.getInt(AppConstants.PATIENT_UUID)
        )?.enqueue(RetrofitMainCallback(getCriticalCareChartHeadingsRespCallback))
    }

}