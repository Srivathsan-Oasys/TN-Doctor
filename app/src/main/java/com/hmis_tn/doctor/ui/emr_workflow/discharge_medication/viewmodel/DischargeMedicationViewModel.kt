package com.hmis_tn.doctor.ui.emr_workflow.discharge_medication.viewmodel

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
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.model.*
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class DischargeMedicationViewModel(application: Application) : AndroidViewModel(application) {
    private var department_UUID: Int? = 0
    private var facility_UUID: Int? = 0
    var appPreferences: AppPreferences? = null
    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var StoreMasterID: Int? = 0

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

        apiService?.getFavourites(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_UUID!!,
            departmentID!!,
            AppConstants.FAV_TYPE_ID_PRESCRIPTION
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


        apiService?.getPrescriptionTemplete(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_UUID!!,
            department_UUID!!,
            AppConstants.FAV_TYPE_ID_PRESCRIPTION
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


}