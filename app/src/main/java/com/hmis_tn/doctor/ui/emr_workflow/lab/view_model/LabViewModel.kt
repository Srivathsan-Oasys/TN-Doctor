package com.hmis_tn.doctor.ui.emr_workflow.lab.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitMainCallback
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppConstants.FAV_TYPE_ID_LAB
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.duration.DurationResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.LabFavResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.LabToLocationResponse
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.LabTypeResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.favresponse.FavSearchResponce
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.template.gettemplate.ResponseLabGetTemplateDetails
import com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.model.LabModifiyRequest
import com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.model.LabModifiyResponse
import com.hmis_tn.doctor.ui.emr_workflow.model.EMR_Request.EmrRequestModel
import com.hmis_tn.doctor.ui.emr_workflow.model.EMR_Response.EmrResponceModel
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.CreateEncounterRequestModel
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.Encounter
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.EncounterDoctor
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.templete.TempleResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.radiology.model.GetToLocationTestResponse
import com.hmis_tn.doctor.ui.quick_reg.model.ResponseTestMethod
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class LabViewModel(
    application: Application
) : AndroidViewModel(
    application
) {
    private var department_UUID: Int? = 0
    private var facility_UUID: Int? = 0
    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var appPreferences: AppPreferences? = null

    var warduuid: Int? = null
    var encounterType: Int? = null

    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)

        warduuid = appPreferences?.getInt(AppConstants.WARDUUID)!!
        encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)!!
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
    }

    fun getFavourites(
        emrWorkFlowRetrofitCallBack: RetrofitCallback<LabFavResponseModel>,
        facility_id: Int?,
        departmentID: Int?
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getLabFavourites(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!,
            departmentID!!,
            FAV_TYPE_ID_LAB
        )?.enqueue(RetrofitMainCallback(emrWorkFlowRetrofitCallBack))
        return
    }

    /*
    Templete
     */
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
            facility_UUID!!,
            department_UUID!!,
            FAV_TYPE_ID_LAB
        )?.enqueue(RetrofitMainCallback(templeteRetrofitCallBack))
        return
    }

    fun getDuration(getDurationRetrofitCallBack: RetrofitCallback<DurationResponseModel>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getDuration(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!
        )?.enqueue(RetrofitMainCallback(getDurationRetrofitCallBack))
        return
    }

    fun getToLocationTest(
        labToRetrofitCallback: RetrofitCallback<GetToLocationTestResponse>,
        facilityID: Int?, department_UUID: Int?, Str_auto_id: Int?
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("test_master_uuid", Str_auto_id)
            jsonBody.put("from_department_uuid", department_UUID)
            jsonBody.put("profile_uuid", null)


            if (encounterType == AppConstants.TYPE_IN_PATIENT) {

                jsonBody.put("ward_uuid", warduuid)


            }


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
        apiService?.getToLocationLabTest(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!, body
        )?.enqueue(RetrofitMainCallback(labToRetrofitCallback))
        return

    }

    fun getCodeSearchResult(
        facility_id: Int?,
        query: String,
        CodeSearchRetrofitCallBack: RetrofitCallback<FavSearchResponce>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            // jsonBody.put("sortField", "uuid")
            jsonBody.put("sortOrder", "DESC")
            jsonBody.put("search", query)
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
        apiService?.getLAbSearchResult(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, body
        )?.enqueue(RetrofitMainCallback(CodeSearchRetrofitCallBack))
        return
    }


    fun getComplaintSearchResult(
        facility_id: Int?,
        query: String,
        complaintSearchRetrofitCallBack: RetrofitCallback<FavSearchResponce>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("sortField", "uuid")
            jsonBody.put("sortOrder", "DESC")
            jsonBody.put("search", query)
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
        apiService?.getLAbSearchResult(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(complaintSearchRetrofitCallBack))
        return
    }

    fun getLabType(
        labTypeRetrofitCallback: RetrofitCallback<LabTypeResponseModel>,
        facilityID: Int?
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("table_name", "order_priority")
            jsonBody.put("sortField", "uuid")
            jsonBody.put("sortOrder", "DESC")
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

        apiService?.getLabType(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!,
            body
        )?.enqueue(RetrofitMainCallback(labTypeRetrofitCallback))
        return

    }

    fun getMasterLocation(
        labToRetrofitCallback: RetrofitCallback<LabToLocationResponse>,
        facilityID: Int?
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getToLocation(
            "en",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!
        )?.enqueue(RetrofitMainCallback(labToRetrofitCallback))
        return

    }

    fun labInsert(
        facility_id: Int?,
        EmrRequestData: EmrRequestModel,
        configFinalRetrofitCallBack: RetrofitCallback<EmrResponceModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.Emrpost(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            EmrRequestData
        )?.enqueue(RetrofitMainCallback(configFinalRetrofitCallBack))
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

    fun getTemplateDetails(
        templateId: Int?,
        facilityUuid: Int?,
        departmentUuid: Int?,
        getTemplateRetrofitCallback: RetrofitCallback<ResponseLabGetTemplateDetails>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.getLastTemplate(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityUuid!!,
            templateId!!,
            AppConstants.FAV_TYPE_ID_LAB,
            department_UUID!!
        )?.enqueue(RetrofitMainCallback(getTemplateRetrofitCallback))
        return
    }

    fun getMethod(
        tablename: String,
        ResponseTestMethodRetrofitCallback: RetrofitCallback<ResponseTestMethod>
    ) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {
            jsonBody.put("pageNo", 0)
            jsonBody.put("paginationSize", 1000)
            jsonBody.put("status", 1)
            jsonBody.put("table_name", tablename)
            jsonBody.put("sortOrder", "ASC")
            jsonBody.put("sortField", "display_order")
            jsonBody.put("search", "")


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

        apiService?.getTestMethod(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_UUID!!,
            body
        )?.enqueue(RetrofitMainCallback(ResponseTestMethodRetrofitCallback))

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


    fun updateLab(
        facility_id: Int?,
        labModifiyRequest: LabModifiyRequest,
        configFinalRetrofitCallBack: RetrofitCallback<LabModifiyResponse>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.lmisLabUpdate(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            labModifiyRequest
        )?.enqueue(RetrofitMainCallback(configFinalRetrofitCallBack))
        return

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