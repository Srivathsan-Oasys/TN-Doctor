package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.view_model

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
import com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.nurse_desk.BedDetailsResponseModel
import com.hmis_tn.doctor.utils.Utils

class BedViewModel(
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
    var facility_id: Int? = 0
    var patient_id: Int? = 0
    var wardUUID: Int? = 0

    init {
        errorTextVisibility.value = 8
        progress.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        patient_id = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        //wardUUID = appPreferences?.getInt(AppConstants.WARDUUID)!!
    }

    fun getBedDetails(
        wardid: Int,
        getbedListRetrofitCallBack: RetrofitCallback<BedDetailsResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getBedDetails(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!,
            wardid
        )!!.enqueue(
            RetrofitMainCallback(getbedListRetrofitCallBack)
        )
    }
}