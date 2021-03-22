package com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.viewmodel

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
import com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.model.SpecialitySketchPrevResponseModel
import com.oasys.digihealth.doctor.utils.Utils

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