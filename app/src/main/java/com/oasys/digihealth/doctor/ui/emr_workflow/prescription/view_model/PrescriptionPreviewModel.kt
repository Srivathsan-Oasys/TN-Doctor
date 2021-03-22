package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.application.HmisApplication
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitMainCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model.PrevPrescriptionModel
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class PrescriptionPreviewModel(
    application: Application
) : AndroidViewModel(application) {
    var progressBar = MutableLiveData<Int>()
    var errorText = MutableLiveData<String>()
    private var noInternetLayout: MutableLiveData<Int>? = null
    var errorTextVisibility = MutableLiveData<Int>()

    var getType: Int? = null

    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var appPreferences: AppPreferences? = null

    init {
        errorTextVisibility.value = 8
        progressBar.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application)

        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)

        getType = appPreferences?.getInt(AppConstants.PRESCRIPTIONTYPE)

    }

    fun getPrevPrescription_Records(
        patientId: Int?,
        facilityid: Int?,
        prevPrescriptionrecordsRetrofitCallback: RetrofitCallback<PrevPrescriptionModel>
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

            jsonBody.put("is_discharge_medication", getType != 0)

            /* if(getType==0) {

             }
             else
             {

                 jsonBody.put("patient_uuid", patientId)

                 jsonBody.put("is_discharge_medication", true)
             }*/

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
        apiService?.getPrevPrescription(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(prevPrescriptionrecordsRetrofitCallback))
    }


}