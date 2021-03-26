package com.hmis_tn.doctor.ui.emr_workflow.diet.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitMainCallback
import com.hmis_tn.doctor.ui.emr_workflow.diet.model.GetPreviousDietOrderResp
import com.hmis_tn.doctor.ui.emr_workflow.diet.model.request.GetPreviousDietOrderReq
import com.hmis_tn.doctor.utils.Utils

class PreviousDietViewModel(application: Application) : AndroidViewModel(application) {

    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    init {
        progress.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)

    }

    fun getPreviousDiet(
        facility_uuid: Int,
        getPreviousDietOrderReq: GetPreviousDietOrderReq,
        getPreviousDietOrderRespCallback: RetrofitCallback<GetPreviousDietOrderResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getPreviousDietOrder(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            getPreviousDietOrderReq
        )?.enqueue(RetrofitMainCallback(getPreviousDietOrderRespCallback))
    }
}