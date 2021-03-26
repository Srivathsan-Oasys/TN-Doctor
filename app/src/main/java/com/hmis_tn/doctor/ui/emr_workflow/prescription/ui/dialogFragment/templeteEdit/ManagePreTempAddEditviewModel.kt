package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.templeteEdit

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
import com.hmis_tn.doctor.ui.emr_workflow.prescription.model.PresInstructionResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.model.PrescriptionDurationResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.model.PrescriptionFrequencyResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.model.PrescriptionRoutResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate.request.SaveTemplateRequestModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate.request.SearchPrescriptionResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate.response.SaveTemplateResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.templeteEdit.request.UpdateRequestModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.templeteEdit.response.UpdateResponseModel
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class ManagePreTempAddEditviewModel(
    application: Application
) : AndroidViewModel(application) {
    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var department_UUID: Int? = 0
    var storemasterID: Int? = 0

    init {
        progress.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        storemasterID = appPreferences?.getInt(AppConstants.STOREMASTER_UUID)
    }

    var facilityID = appPreferences?.getInt(AppConstants.FACILITY_UUID)

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

    fun getPrescriptionDuration(
        getFrequencyRetrofitCallback: RetrofitCallback<PrescriptionDurationResponseModel>,
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
        )?.enqueue(RetrofitMainCallback(getFrequencyRetrofitCallback))
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

    fun getSearchDetails(
        searchRetrofitCallback: RetrofitCallback<SearchPrescriptionResponseModel>,
        facilityId: Int?,
        searchData: String
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        val jsonBody = JSONObject()
        try {

            jsonBody.put("itemname", searchData)

            jsonBody.put("store_master_uuid", storemasterID)


        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        userDataStoreBean?.department_uuid

        apiService?.getprescriptionSearch(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!,
            body
        )?.enqueue(RetrofitMainCallback(searchRetrofitCallback))


    }

    fun saveTemplate(
        saveTemplateRetrofitCallback: RetrofitCallback<SaveTemplateResponseModel>,
        saveTemplateRequestModel: SaveTemplateRequestModel
    ) {


        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.savePrescriptionTemplate(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!,
            saveTemplateRequestModel
        )?.enqueue(RetrofitMainCallback(saveTemplateRetrofitCallback))


    }

    fun updateTemplate(
        updateTemplateRetrofitCallback: RetrofitCallback<UpdateResponseModel>,
        updateRequestModel: UpdateRequestModel
    ) {


        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.updatePrescriptionTemplate(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!,
            updateRequestModel
        )?.enqueue(RetrofitMainCallback(updateTemplateRetrofitCallback))

    }


}