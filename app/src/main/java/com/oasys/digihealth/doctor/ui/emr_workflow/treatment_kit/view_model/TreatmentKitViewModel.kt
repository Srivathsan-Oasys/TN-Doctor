package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.view_model

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
import com.oasys.digihealth.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.model.diagonosissearch.DiagonosisSearchResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.InvestigationLoationResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.InvestigationTypeResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.LabToLocationResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.LabTypeResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.FavSearchResponce
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.CreateEncounterRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.Encounter
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.EncounterDoctor
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.GetToLocationTestResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.RadiologyTypeResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.OrderSaveTreatmentKitResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.TreatmentKitPrevResponsModel
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.TreatmentkitSearchResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.drug_info.GetDrugInfoReq
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.drug_info.GetDrugInfoResp
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.investigation_code_search.GetInvestigationCodeSearchReq
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.investigation_code_search.GetInvestigationCodeSearchResp
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.modify.UpdateTreatmentKitPreviousOrderReq
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.modify.UpdateTreatmentKitPreviousOrderResp
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.radiology_code_search.GetRadiologyCodeSearchReq
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.radiology_code_search.GetRadiologyCodeSearchResp
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.request.TKOrderRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.response.FavouriteAddToListResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.response.TreatmentPrescRouteSpinnerResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.search.AutoSearchReq
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.search.AutoSearchResp
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.search.TreatmentKitFavouriteResp
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class TreatmentKitViewModel(
    application: Application
) : AndroidViewModel(
    application
) {
    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var appPreferences: AppPreferences? = null
    var StoreMasterID: Int? = 0
    var FacilityID: Int? = 0
    var department_UUID: Int? = 0
    var warduuid: Int? = null
    var encounterType: Int? = null

    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        StoreMasterID = appPreferences?.getInt(AppConstants.STOREMASTER_UUID)
        FacilityID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)!!
        warduuid = appPreferences?.getInt(AppConstants.WARDUUID)!!
    }

    fun getPrescriptionFavourites(
        departmentID: Int?,
        emrWorkFlowRetrofitCallBack: RetrofitCallback<FavouritesResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getFavourites(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            FacilityID!!,
            departmentID!!,
            AppConstants.FAV_TYPE_ID_PRESCRIPTION
        ).enqueue(RetrofitMainCallback(emrWorkFlowRetrofitCallBack))
        return
    }

    fun getdiagnosisFavourites(
        departmentId: Int?,
        favouritesRetrofitCallBack: RetrofitCallback<FavouritesResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getTKFavourite(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, userDataStoreBean?.uuid!!,
            FacilityID!!,
            departmentId.toString()
        ).enqueue(RetrofitMainCallback(favouritesRetrofitCallBack))
        return
    }

    fun getDiagnosisComplaintSearchResult(
        facilityUuid: Int?,
        query: String,
        dignosisSearchRetrofitCallBack1: RetrofitCallback<DiagonosisSearchResponse>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getDiagonosisName(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, userDataStoreBean?.uuid!!,
            facilityUuid!!,
            "filterbythree", query
        ).enqueue(RetrofitMainCallback(dignosisSearchRetrofitCallBack1))
    }

    fun getPrescriptionComplaintSearchResult(
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
            jsonBody.put("store_master_uuid", StoreMasterID)

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
        ).enqueue(RetrofitMainCallback(complaintSearchRetrofitCallBack))
        return
    }

    fun getLabComplaintSearchResult(
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
        ).enqueue(RetrofitMainCallback(complaintSearchRetrofitCallBack))
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
        ).enqueue(RetrofitMainCallback(CodeSearchRetrofitCallBack))
        return
    }

    fun getLabToLocation(
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
        ).enqueue(RetrofitMainCallback(labToRetrofitCallback))
        return

    }

    fun getInvestigationToLocation(
        labToRetrofitCallback: RetrofitCallback<InvestigationLoationResponseModel>,
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
        apiService?.getInvestigationToLocation(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!
        ).enqueue(RetrofitMainCallback(labToRetrofitCallback))
        return

    }

    fun getToLocation(
        labToRetrofitCallback: RetrofitCallback<LabToLocationResponse>,
        facilityID: Int?
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0

        val jsonBody = JSONObject()
        try {
            jsonBody.put("pageNo", 0)
            jsonBody.put("paginationSize", 1000)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.GetToLocationTReatmentKit(
            "en",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!,
            body
        ).enqueue(RetrofitMainCallback(labToRetrofitCallback))
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
        ).enqueue(RetrofitMainCallback(labTypeRetrofitCallback))
        return

    }


    fun getRadiologyType(
        radiologyTypeRetrofitCallback: RetrofitCallback<RadiologyTypeResponseModel>,
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

        apiService?.getTKRadioType(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!,
            body
        ).enqueue(RetrofitMainCallback(radiologyTypeRetrofitCallback))
        return

    }

    fun getInvestigationType(
        labTypeRetrofitCallback: RetrofitCallback<InvestigationTypeResponseModel>,
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

        apiService?.getInvestigationType(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!,
            body
        ).enqueue(RetrofitMainCallback(labTypeRetrofitCallback))
        return

    }


    fun getRadioSearchResult(
        facility_id: Int?,
        query: String,
        radioSearchRetrofitCallBack: RetrofitCallback<FavSearchResponce>
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

        apiService?.getRadioSearchResult(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!,
            body
        ).enqueue(RetrofitMainCallback(radioSearchRetrofitCallBack))
        return
    }

    fun getInvestigationComplaintSearchResult(
        facility_id: Int?,
        query: String,
        complaintSearchRetrofitCallBack: RetrofitCallback<TreatmentkitSearchResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
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

        apiService?.getTraetmentkitSearch(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!,
            body
        ).enqueue(RetrofitMainCallback(complaintSearchRetrofitCallBack))
        return
    }


    fun getdiagnosisSearchResult(
        query: String,
        facilityUuid: Int?,
        complaintSearchRetrofitCallBack: RetrofitCallback<DiagonosisSearchResponse>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getDiagonosisName(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, userDataStoreBean?.uuid!!,
            facilityUuid!!, "filterbythree", query
        ).enqueue(
            RetrofitMainCallback(complaintSearchRetrofitCallBack)
        )
        return
    }

    fun getRouteDetails(
        labTypeRetrofitCallback: RetrofitCallback<TreatmentPrescRouteSpinnerResponse>,
        facilityID: Int?
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {

            jsonBody.put("table_name", "drug_route")
            jsonBody.put("paginationSize", 1000)

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

        apiService?.getTKPrescRouteSpinner(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!,
            body
        ).enqueue(RetrofitMainCallback(labTypeRetrofitCallback))
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
        ).enqueue(RetrofitMainCallback(instructionRetrofitCallback))
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
        ).enqueue(RetrofitMainCallback(getFrequencyRetrofitCallback))
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
        ).enqueue(RetrofitMainCallback(getPrescriptionDurationRetrofitCallBack))
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
        ).enqueue(RetrofitMainCallback(deleteRetrofitCallback))
        return
    }

    fun getComplaintSearchResult(
        query: String,
        facilityUuid: Int?,
        complaintSearchRetrofitCallBack: RetrofitCallback<DiagonosisSearchResponse>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getDiagonosisName(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, userDataStoreBean?.uuid!!,
            facilityUuid!!, "filterbythree", query
        ).enqueue(
            RetrofitMainCallback(complaintSearchRetrofitCallBack)
        )
        return
    }

    /*
        fun getPrevTreatment_kit_Records(patientId: Int?, facilityid: Int?,prevLabrecordsRetrofitCallback: RetrofitCallback<PrevLabResponseModel>) {
            if (!Utils.isNetworkConnected(getApplication())) {
                errorText.value = getApplication<Application>().getString(R.string.no_internet)
                return
            }


            val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
            val jsonBody = JSONObject()
            try {
                jsonBody.put("patient_id", patientId)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val body = RequestBody.create(
                okhttp3.MediaType.parse("application/json; charset=utf-8"),
                jsonBody.toString()
            )
            progress.value = 0
            val hmisApplication = HmisApplication.get(getApplication())
            val apiService = hmisApplication.getRetrofitService()
            apiService?.getPrevLab(
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facilityid!!,body)?.enqueue(RetrofitMainCallback(prevLabrecordsRetrofitCallback))
        }
    */
    fun getPrevTreatment_kit_Records(
        patientUuid: Int,
        facility_id: Int?,
        fetchEncounterRetrofitCallBack: RetrofitCallback<TreatmentKitPrevResponsModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getPrevTreatmentKit(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            patientUuid


        )!!.enqueue(
            RetrofitMainCallback(fetchEncounterRetrofitCallBack)
        )
    }

    fun createOrderTreatmentKit(
        facilityID: Int?,
        tkOrderRequestModel: TKOrderRequestModel,
        createTreatmentmentRetrofitCallBack: RetrofitCallback<OrderSaveTreatmentKitResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.orderSave(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityID!!,
            tkOrderRequestModel
        ).enqueue(RetrofitMainCallback(createTreatmentmentRetrofitCallBack))
        return
    }

    fun getFavouritesToList(
        treatmentId: Int?,
        favouriteId: Int?,
        favouritesRetrofitCallBack: RetrofitCallback<FavouriteAddToListResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getFavList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, userDataStoreBean?.uuid!!,
            FacilityID!!,
            treatmentId!!,
            favouriteId!!
        ).enqueue(RetrofitMainCallback(favouritesRetrofitCallBack))
        return
    }

    fun modifyTreatmentKit(
        facility_uuid: Int,
        orderId: Int,
        updateTreatmentKitPreviousOrderReq: UpdateTreatmentKitPreviousOrderReq,
        updateTreatmentKitPreviousOrderRespCallback: RetrofitCallback<UpdateTreatmentKitPreviousOrderResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.modifyTreatmentKit(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            orderId,
            updateTreatmentKitPreviousOrderReq
        ).enqueue(RetrofitMainCallback(updateTreatmentKitPreviousOrderRespCallback))
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
            facilityId!!, userDataStoreBean?.user_name, body
        ).enqueue(RetrofitMainCallback(injectionRetrofitCallback))
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
            FacilityID!!,
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
        ).enqueue(RetrofitMainCallback(labToRetrofitCallback))
        return

    }

    fun treatmentKitAutoSearch(
        facility_uuid: Int,
        autoSearchReq: AutoSearchReq,
        autoSearchRespCallback: RetrofitCallback<AutoSearchResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.treatmentKitAutoSearch(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            autoSearchReq
        ).enqueue(RetrofitMainCallback(autoSearchRespCallback))
    }

    fun treatmentKitFavourite(
        facility_uuid: Int,
        treatmentId: Int,
        favouriteId: String,
        treatmentKitFavouriteRespCallback: RetrofitCallback<TreatmentKitFavouriteResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.treatmentKitFavouriteById(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            treatmentId,
            favouriteId
        ).enqueue(RetrofitMainCallback(treatmentKitFavouriteRespCallback))
    }

    fun getRadiologyCodeSearch(
        facilityUuid: Int,
        getRadiologyCodeSearchReq: GetRadiologyCodeSearchReq,
        getRadiologyCodeSearchRespCallback: RetrofitCallback<GetRadiologyCodeSearchResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0

        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getRadiologyCodeSearch(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityUuid,
            getRadiologyCodeSearchReq
        ).enqueue(RetrofitMainCallback(getRadiologyCodeSearchRespCallback))
    }

    fun getInvestigationCodeSearch(
        facilityUuid: Int,
        getInvestigationCodeSearchReq: GetInvestigationCodeSearchReq,
        getInvestigationCodeSearchRespCallback: RetrofitCallback<GetInvestigationCodeSearchResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0

        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getInvestigationCodeSearch(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityUuid,
            getInvestigationCodeSearchReq
        ).enqueue(RetrofitMainCallback(getInvestigationCodeSearchRespCallback))
    }

    fun getDrugInfo(
        facilityID: Int,
        getDrugInfoReq: GetDrugInfoReq,
        getDrugInfoRespCallback: RetrofitCallback<GetDrugInfoResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getDrugInfo(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID,
            getDrugInfoReq
        ).enqueue(RetrofitMainCallback(getDrugInfoRespCallback))
    }
}