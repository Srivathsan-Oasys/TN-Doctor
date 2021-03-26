package com.hmis_tn.doctor.ui.emr_workflow.lab_result.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitMainCallback
import com.hmis_tn.doctor.ui.emr_workflow.lab_result.model.LabResultResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.lab_result.model.compare.LabCompareResp
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class LabResultViewModel(
    application: Application
) : AndroidViewModel(
    application
) {
    private var department_UUID: Int? = 0
    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var errorTextVisibility = MutableLiveData<Int>()
    var appPreferences: AppPreferences? = null

    init {
        errorTextVisibility.value = 8
        progress.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
    }

    fun getLab_Result(
        patientId: String?,
        facilityid: Int?,
        fromDate: String,
        toDate: String,
        LabResultRetrofitCallback: RetrofitCallback<LabResultResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        Log.i("", "" + patientId)
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
        apiService?.getLabResult(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(LabResultRetrofitCallback))
    }

    fun getLabCompare(
        patientId: String?,
        facilityid: Int?,
        dateArray: ArrayList<String>,
        masterId: String?,
        LabResultCompareRetrofitCallback: RetrofitCallback<LabCompareResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        Log.i("", "" + patientId)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("patient_id", patientId)
            jsonBody.put("test_master_uuid", masterId)
            val jsonArray = JSONArray()
            dateArray.forEach { s: String ->
                jsonArray.put(s)
            }
            jsonBody.put("order_request_date", jsonArray)
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
        apiService?.getLabResultCompere(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(LabResultCompareRetrofitCallback))
    }

    fun getLab_ResultByDate(
        patientId: String?,
        facilityid: Int?,
        fromDate: String,
        toDate: String,
        LabResultRetrofitCallback: RetrofitCallback<LabResultResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        Log.i("", "" + patientId)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("patient_id", patientId)
            jsonBody.put("fromDate", fromDate)
            jsonBody.put("toDate", toDate)
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
        apiService?.getLabResult(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(LabResultRetrofitCallback))
    }
}