package com.hmis_tn.doctor.ui.emr_workflow.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppConstants.BEARER_AUTH
import com.hmis_tn.doctor.config.AppConstants.DEPARTMENT_UUID
import com.hmis_tn.doctor.config.AppConstants.FACILITY_UUID
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitMainCallback
import com.hmis_tn.doctor.ui.emr_workflow.model.EmrWorkFlowResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.GetStoreMasterResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.PatientDetailResponse
import com.hmis_tn.doctor.ui.emr_workflow.model.PatientLatestRecordResponse
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.CreateEncounterRequestModel
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.Encounter
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.EncounterDoctor
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class EmrViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {
    var errorText = MutableLiveData<String>()
    var progressBar = MutableLiveData<Int>()
    private var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var appPreferences: AppPreferences? = null

    init {
        progressBar.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
    }

    fun getEmrWorkFlow(
        emrWorkFlowRetrofitCallBack: RetrofitCallback<EmrWorkFlowResponseModel>,
        contextUuid: Int
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getEmrWorkflowForIpAndOp(
            BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, contextUuid
        )!!.enqueue(
            RetrofitMainCallback(emrWorkFlowRetrofitCallBack)
        )
    }


    fun getEncounter(
        facility_id: Int,
        patientUuid: Int,
        encounterType: Int,
        fetchEncounterRetrofitCallBack: RetrofitCallback<FectchEncounterResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getEncounters(
            BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id,
            patientUuid,
            userDataStoreBean.uuid,
            appPreferences?.getInt(DEPARTMENT_UUID)!!,
            encounterType

        )!!.enqueue(
            RetrofitMainCallback(fetchEncounterRetrofitCallBack)
        )
    }

    fun createEncounter(
        patientUuid: Int,
        encounterType: Int,
        createEncounterCallback: RetrofitCallback<CreateEncounterResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        val createEncounterRequestModel = CreateEncounterRequestModel()

        val encounter = Encounter()
        encounter.admission_request_uuid = 0
        encounter.admission_uuid = 0
        encounter.appointment_uuid = 0
        encounter.department_uuid = appPreferences?.getInt(
            DEPARTMENT_UUID
        )
        encounter.discharge_type_uuid = 0
        encounter.encounter_identifier = 0
        encounter.encounter_priority_uuid = 0
        encounter.encounter_status_uuid = 0
        encounter.encounter_type_uuid = encounterType
        encounter.facility_uuid = appPreferences?.getInt(FACILITY_UUID)
        encounter.patient_uuid = patientUuid

        createEncounterRequestModel.encounter = encounter

        val encounterDoctor = EncounterDoctor()
        encounterDoctor.department_uuid = appPreferences?.getInt(DEPARTMENT_UUID)
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
            BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            createEncounterRequestModel

        )!!.enqueue(
            RetrofitMainCallback(createEncounterCallback)
        )
    }

    fun getStoreMaster(
        facility_uuid: Int,
        department_uuid: Int,
        getStoreMasterRetrofitCallBack: RetrofitCallback<GetStoreMasterResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("department_uuid", department_uuid)
            jsonBody.put("facility_uuid", facility_uuid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getStoreMaster(
            BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid, body
        )?.enqueue(RetrofitMainCallback(getStoreMasterRetrofitCallBack))
        return
    }


    fun getPatientLatestRecord(
        facility_uuid: Int,
        patientUuid: Int,
        encounterType: Int,
        getPatientLatestEncCallback: RetrofitCallback<PatientLatestRecordResponse>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getLatestRecordById(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            userDataStoreBean.user_name,
            patientUuid,
            encounterType
        )?.enqueue(RetrofitMainCallback(getPatientLatestEncCallback))
        return
    }


    fun getPatientById(
        facility_uuid: Int,
        patientUuid: Int,
        encounterType: Int,
        getPatientByIdCallback: RetrofitCallback<PatientDetailResponse>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("patientId", patientUuid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getPatientById(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_uuid, userDataStoreBean.user_name, body
        )?.enqueue(RetrofitMainCallback(getPatientByIdCallback))
        return
    }

    fun getEmrWorkFlowFav(
        emrWorkFlowRetrofitCallBack: RetrofitCallback<EmrWorkFlowResponseModel>,
        facility_uuid: Int
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getWorkFlowNurseGetList(
            "en",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_uuid
        )!!.enqueue(
            RetrofitMainCallback(emrWorkFlowRetrofitCallBack)
        )
    }

}