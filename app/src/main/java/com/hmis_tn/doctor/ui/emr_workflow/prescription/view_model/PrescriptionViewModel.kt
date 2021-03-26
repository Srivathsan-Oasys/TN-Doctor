package com.hmis_tn.doctor.ui.emr_workflow.prescription.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitMainCallback
import com.hmis_tn.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.CreateEncounterRequestModel
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.Encounter
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.EncounterDoctor
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.model.*
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject

class PrescriptionViewModel(application: Application) : AndroidViewModel(application) {
    private var department_UUID: Int? = 0
    private var facility_UUID: Int? = 0
    var appPreferences: AppPreferences? = null
    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var StoreMasterID: Int? = null

    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        StoreMasterID = appPreferences?.getInt(AppConstants.STOREMASTER_UUID)
    }

    fun getFavourites(
        emrWorkFlowRetrofitCallBack: RetrofitCallback<FavouritesResponseModel>,
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

        apiService?.getPrescriptionFavourites(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_UUID!!, departmentID!!,
            AppConstants.FAV_TYPE_ID_PRESCRIPTION, StoreMasterID
        )?.enqueue(RetrofitMainCallback(emrWorkFlowRetrofitCallBack))
        return
    }

    fun getTemplete(templeteRetrofitCallBack: RetrofitCallback<PrescriptionTemplateResponseModel>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()


        apiService?.getPrescriptionStoreTemplete(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_UUID!!,
            department_UUID!!,
            AppConstants.FAV_TYPE_ID_PRESCRIPTION,
            StoreMasterID
        )?.enqueue(RetrofitMainCallback(templeteRetrofitCallBack))
        return
    }

    fun getRouteDetails(
        labTypeRetrofitCallback: RetrofitCallback<PrescriptionRoutResponseModel>,
        facilityID: Int?
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {

            jsonBody.put("table_name", "drug_route")
            jsonBody.put("paginationSize", 100)
            jsonBody.put("sortOrder", "ASC")


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

        apiService?.getRouteDetails(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!,
            body
        )?.enqueue(RetrofitMainCallback(labTypeRetrofitCallback))
        return

    }

    fun getInstructionDetails(
        instructionRetrofitCallback: RetrofitCallback<PresInstructionResponseModel>,
        facilityID: Int?
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {

            jsonBody.put("table_name", "drug_instruction")
            jsonBody.put("paginationSize", 100)
            jsonBody.put("sortOrder", "ASC")

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

        apiService?.getInstruction(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!,
            body
        )?.enqueue(RetrofitMainCallback(instructionRetrofitCallback))
        return

    }

    fun getFrequencyDetails(
        getFrequencyRetrofitCallback: RetrofitCallback<PrescriptionFrequencyResponseModel>,
        facilityID: Int?
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("paginationSize", 100)
            jsonBody.put("sortOrder", "ASC")

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
        apiService?.getPrescFrequency(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!, body
        )?.enqueue(RetrofitMainCallback(getFrequencyRetrofitCallback))
        return

    }

    fun getComplaintSearchResult(
        query: String,
        complaintSearchRetrofitCallBack: RetrofitCallback<PrescriptionSearchResponseModel>,
        facilityID: Int?
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("itemname", query)
            jsonBody.put("store_master_uuid", StoreMasterID.toString())

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

        apiService?.getPrescriptionsSearchResult(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!,
            body
        )?.enqueue(RetrofitMainCallback(complaintSearchRetrofitCallBack))
        return
    }

    fun getPrescriptionDuration(
        getPrescriptionDurationRetrofitCallBack: RetrofitCallback<PrescriptionDurationResponseModel>,
        facilityID: Int?
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("table_name", "duration_period")

            jsonBody.put("paginationSize", 100)
            jsonBody.put("sortOrder", "ASC")


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
        apiService?.getPrescriptionDuration(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!, body
        )?.enqueue(RetrofitMainCallback(getPrescriptionDurationRetrofitCallBack))
        return

    }

    fun prescriptiondataInsert(
        facilityId: Int?,
        emrPrescriptionRequestModel: emr_prescription_postalldata_requestmodel,
        emrprescriptionpostRetrofitCallback: RetrofitCallback<PrescriptionPostAllDataResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        Log.e("PrescPost", emrPrescriptionRequestModel.toString())

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.EmrPrescriptionPost(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityId!!,
            emrPrescriptionRequestModel
        )?.enqueue(RetrofitMainCallback(emrprescriptionpostRetrofitCallback))
        return


    }

    fun prescriptiondataUpdate(
        facilityId: Int?,
        emrPrescriptionRequestModel: PrescriptionUpdateRequest,
        emrprescriptionpostRetrofitCallback: RetrofitCallback<PrescriptionPostAllDataResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        //  Log.e("PrescPost",emrPrescriptionRequestModel.toString())

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.EmrPrescriptionUpdate(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityId!!,
            emrPrescriptionRequestModel
        )?.enqueue(RetrofitMainCallback(emrprescriptionpostRetrofitCallback))
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

    fun getInjectionDetails(
        injectionRetrofitCallback: RetrofitCallback<InjectionDepartMentResponce>,
        facilityId: Int?
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("department_uuid", department_UUID)
            jsonBody.put("gender_uuid", 1)
            //  jsonBody.put("store_type_uuid", StoreMasterID)

            jsonBody.put("store_type_uuid", 7)


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
        apiService?.getInjectionType(
            AppConstants.LANGUAGE,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId!!, userDataStoreBean.user_name, body
        )?.enqueue(RetrofitMainCallback(injectionRetrofitCallback))
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

    fun GetPrescriptionPDF(
        prescriptionUuid: Int,
        pdfGenerateRetrofitCallback: RetrofitCallback<ResponseBody>
    ) {


        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("Id", prescriptionUuid)


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
        apiService?.getPrescriptionPDF(
            AppConstants.LANGUAGE,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_UUID!!,
            body
        )?.enqueue(RetrofitMainCallback(pdfGenerateRetrofitCallback))
        return
    }

}