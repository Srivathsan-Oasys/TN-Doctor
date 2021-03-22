package com.oasys.digihealth.doctor.ui.emr_workflow.history.diagnosis.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.application.HmisApplication
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppConstants.BEARER_AUTH
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitMainCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.history.diagnosis.model.DiagnosisSearchResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.diagnosis.model.HistoryDiagnosisCreateResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.diagnosis.model.HistoryDiagnosisResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.diagnosis.model.HistoryDiagnosisUpdateResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.CreateEncounterRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.Encounter
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.EncounterDoctor
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody

class HistoryDiagnosisViewModel(application: Application) : AndroidViewModel(application) {

    var errorText = MutableLiveData<String>()

    var progress = MutableLiveData<Int>()

    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    var appPreferences: AppPreferences? = null

    init {

        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)

    }


    fun getlist(
        facilityId: Int?,
        patient_id: Int?,
        department_uuid: Int?,
        getallListRetrofitCallback: RetrofitCallback<HistoryDiagnosisResponseModel>
    ) {


        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (facilityId != null) {
            if (patient_id != null) {
                apiService?.getDiagnosisList(
                    BEARER_AUTH + userDataStoreBean?.access_token,
                    userDataStoreBean?.uuid!!, facilityId, "getLatestDiagnosis", 10,
                    patient_id, department_uuid!!
                )?.enqueue(
                    RetrofitMainCallback(getallListRetrofitCallback)
                )
            }
        }
        return
    }


    fun getComplaintSearchResult(
        facility_uuid: Int?,
        query: String,
        dignosisSearchRetrofitCallBack1: RetrofitCallback<DiagnosisSearchResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.searchDiagnosis(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid!!,
            "filterbythree", query
        )?.enqueue(RetrofitMainCallback(dignosisSearchRetrofitCallBack1))
    }


    fun postDiagnosisData(
        facility_uuid: Int,
        diagnosisRequest: RequestBody,
        postAllergyRetrofitCallBack: RetrofitCallback<HistoryDiagnosisCreateResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.createDiagnosis(
            BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid, diagnosisRequest
        )?.enqueue(RetrofitMainCallback(postAllergyRetrofitCallBack))
        return
    }


    fun updateDiagnosisData(
        facility_uuid: Int, patient_diagnosis_id: Int?,
        patient_uuid: Int?,
        department_uuid: Int?,
        diagnosisupdateRequest: RequestBody,
        updateDiagnosisRetrofitCallBack: RetrofitCallback<HistoryDiagnosisUpdateResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.updateDiagnosis(
            BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            patient_diagnosis_id!!,
            patient_uuid!!,
            department_uuid!!,
            diagnosisupdateRequest
        )?.enqueue(RetrofitMainCallback(updateDiagnosisRetrofitCallBack))
        return
    }

    fun getEncounter(
        facilityId: Int,
        patientUuid: Int,
        encounterType: Int,
        fetchEncounterRetrofitCallBack: RetrofitCallback<FectchEncounterResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getEncounters(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId,
            patientUuid,
            userDataStoreBean.uuid,
            appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!,
            encounterType

        )!!.enqueue(
            RetrofitMainCallback(fetchEncounterRetrofitCallBack)
        )
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
}