package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.view_model

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
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model.PrescriptionInfoResponsModel
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class CommentViewModel(application: Application) : AndroidViewModel(application) {
    private var department_UUID: Int? = 0
    var appPreferences: AppPreferences? = null
    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
    }


    fun getPrescriptionInfoDetails(
        facility_id: Int?,
        prescriptionInfoRetrofitCallback: RetrofitCallback<PrescriptionInfoResponsModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("Id", 267)
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
        apiService?.getPrescriptionInfo(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, body
        )?.enqueue(RetrofitMainCallback(prescriptionInfoRetrofitCallback))
        return
    }


}