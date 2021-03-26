package com.hmis_tn.doctor.ui.emr_workflow.history.prescription.view_model


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
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.duration.DurationResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.history.prescription.model.PrescriptionHistoryResponseModel
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject


class HistoryPrescriptionViewModel(
    application: Application
) : AndroidViewModel(
    application
) {
    var progressBar = MutableLiveData<Int>()
    var errorText = MutableLiveData<String>()
    private var noInternetLayout: MutableLiveData<Int>? = null
    var errorTextVisibility = MutableLiveData<Int>()

    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var appPreferences: AppPreferences? = null


    init {
        errorTextVisibility.value = 8
        progressBar.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
    }

    fun getPrescription_History(
        patientId: Int?,
        facilityid: Int?,
        prevPrescriptionrecordsRetrofitCallback: RetrofitCallback<PrescriptionHistoryResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        Log.i("", "" + patientId)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("patient_uuid", patientId)
            jsonBody.put("encounter_type_uuid", "1")


        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        progressBar.value = 0
        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()
        apiService?.getHistoryPrescription(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(prevPrescriptionrecordsRetrofitCallback))
    }

    fun getDuration(getDurationRetrofitCallBack: RetrofitCallback<DurationResponseModel>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getDuration(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!
        )?.enqueue(RetrofitMainCallback(getDurationRetrofitCallBack))
        return
    }


}