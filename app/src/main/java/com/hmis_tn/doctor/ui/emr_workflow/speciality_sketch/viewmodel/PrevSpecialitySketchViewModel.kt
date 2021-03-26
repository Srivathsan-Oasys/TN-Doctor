package com.hmis_tn.doctor.ui.emr_workflow.speciality_sketch.viewmodel

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
import com.hmis_tn.doctor.ui.emr_workflow.speciality_sketch.model.SpecialitySketchPrevResponseModel
import com.hmis_tn.doctor.utils.Utils

class PrevSpecialitySketchViewModel(application: Application) : AndroidViewModel(application) {

    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    init {
        progress.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)

    }

    fun getPreviousSpecialitySketch(
        facility_uuid: Int,
        patientId: Int,
        getPreviousDietOrderRespCallback: RetrofitCallback<SpecialitySketchPrevResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getPreviousSpecialitySketch(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid, patientId
        )?.enqueue(RetrofitMainCallback(getPreviousDietOrderRespCallback))
    }
}