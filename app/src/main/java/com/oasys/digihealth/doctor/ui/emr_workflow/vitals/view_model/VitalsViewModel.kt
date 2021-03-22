package com.oasys.digihealth.doctor.ui.emr_workflow.vitals.view_model


//import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.UOMListResponseModel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.application.HmisApplication
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitMainCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.CreateEncounterRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.Encounter
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.EncounterDoctor
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempleResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.VitalSaveRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.VitalsTemplateResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.previous_vitals.GetPrevPatientVitalResp
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.response.VitalSaveResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.response.VitalSearchListResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.responseedittemplatevitual.ResponseEditTemplate
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.responseuommodule.ResponseUOMModules
import com.oasys.digihealth.doctor.ui.nursedesk.nurse_desk_vitals.ui.nextstepvital.model.MainVItalsListResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class VitalsViewModel(
    application: Application
) : AndroidViewModel(
    application
) {
    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var appPreferences: AppPreferences? = null
    private var department_UUID: Int? = 0
    private var facility_UUID: Int? = 0
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)

    }

    fun getvitalsTemplate(
        faciltyID: Int?,
        departmentId: Int?,
        vitalsTemplateRetrofitCallBack: RetrofitCallback<VitalsTemplateResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getVitalsTemplatet(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, userDataStoreBean?.uuid!!,
            faciltyID!!, departmentId!!, AppConstants.TEM_TYPE_ID_VITALS, userDataStoreBean.uuid
        )?.enqueue(RetrofitMainCallback(vitalsTemplateRetrofitCallBack))
        return
    }

    fun getUmoList(faciltyID: Int?, umoListRetrofitCallback: RetrofitCallback<ResponseUOMModules>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        // api call for send otp

        val jsonBody = JSONObject()
        try {
            jsonBody.put("sortField", "name")
            jsonBody.put("sortOrder", "ASC")
            jsonBody.put("table_name", "emr_uom")
            jsonBody.put("paginationSize", 100)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getUOMValue(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, userDataStoreBean?.uuid!!,
            faciltyID!!, body
        )?.enqueue(RetrofitMainCallback(umoListRetrofitCallback))
        return

    }

    /*fun getVitalsName(faciltyID: Int?, vitalsNametRetrofitCallback: RetrofitCallback<VitalsListResponseModel>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }


        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getVitalsName(
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, userDataStoreBean?.uuid!!,
                faciltyID!!
        )?.enqueue(RetrofitMainCallback(vitalsNametRetrofitCallback))
        return

    }*/

    fun getVitalsName(
        faciltyID: Int?,
        vitalsNametRetrofitCallback: RetrofitCallback<MainVItalsListResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getVitalsList(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, userDataStoreBean?.uuid!!,
            faciltyID!!
        ).enqueue(RetrofitMainCallback(vitalsNametRetrofitCallback))
        return

    }


    fun deleteFavourite(
        facility_id: Int?,
        favouriteId: Int?,
        deleteRetrofitCallback: RetrofitCallback<DeleteResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("favouriteId", favouriteId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.deleteRows(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, body
        )?.enqueue(RetrofitMainCallback(deleteRetrofitCallback))
        return
    }

    fun vitalSave(
        faciltyID: Int?,
        vitalsSaveRetrofitCallback: RetrofitCallback<VitalSaveResponseModel>,
        saveData: ArrayList<VitalSaveRequestModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.saveVitals(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, faciltyID!!, saveData
        )?.enqueue(RetrofitMainCallback(vitalsSaveRetrofitCallback))
        return

    }

    fun searchList(
        vitalSearchRetrofitCallback: RetrofitCallback<VitalSearchListResponseModel>,
        faciltyID: Int?
    ) {


        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.getVitals(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, faciltyID!!
        )?.enqueue(RetrofitMainCallback(vitalSearchRetrofitCallback))
        return

    }

    fun getTemplateDetails(
        templateId: Int?,
        facilityUuid: Int?,
        departmentUuid: Int?,
        getTemplateRetrofitCallback: RetrofitCallback<ResponseEditTemplate>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.getLastVitualTemplate(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityUuid!!,
            templateId!!,
            AppConstants.FAV_TYPE_ID_Vitual,
            departmentUuid!!
        )?.enqueue(RetrofitMainCallback(getTemplateRetrofitCallback))
        return
    }


    fun deleteTemplate(
        facility_id: Int?,
        template_uuid: Int?,
        deleteRetrofitCallback: RetrofitCallback<DeleteResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("template_uuid", template_uuid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.deleteTemplate(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, body
        )?.enqueue(RetrofitMainCallback(deleteRetrofitCallback))
        return
    }

    fun getTemplete(templeteRetrofitCallBack: RetrofitCallback<TempleResponseModel>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()


        apiService?.getTemplete(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            department_UUID!!,
            facility_UUID!!,
            AppConstants.FAV_TYPE_ID_Vitual
        )?.enqueue(RetrofitMainCallback(templeteRetrofitCallBack))
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
            facility_UUID!!,
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

    fun getPrevPatientVitals(
        facility_uuid: Int,
        patientUuid: Int,
        departmentUuid: Int,
        getPrevPatientVitalRespRetrofitCallback: RetrofitCallback<GetPrevPatientVitalResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getPrevPatientVital(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            patientUuid,
            departmentUuid
        )?.enqueue(RetrofitMainCallback(getPrevPatientVitalRespRetrofitCallback))
    }
}



