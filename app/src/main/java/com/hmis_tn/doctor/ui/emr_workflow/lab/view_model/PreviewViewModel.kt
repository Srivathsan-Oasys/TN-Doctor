package com.hmis_tn.doctor.ui.emr_workflow.view.lab.view_model

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
import com.hmis_tn.doctor.ui.emr_workflow.view.lab.model.PrevLabResponseModel
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class PreviewViewModel(
    application: Application,
    val prevLabrecordsRetrofitCallback: RetrofitCallback<PrevLabResponseModel>
) : AndroidViewModel(application) {
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

    }

    fun getPrevLab_Records(patientId: Int?, facilityid: Int?) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("patient_id", patientId)
            jsonBody.put("lab_master_type_uuid", AppConstants.LAB_TESTMASTER_UUID)
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
        apiService?.getPrevLab(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(prevLabrecordsRetrofitCallback))
    }


}