package com.hmis_tn.doctor.ui.emr_workflow.history.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.config.AppConstants.BEARER_AUTH
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitMainCallback
import com.hmis_tn.doctor.ui.emr_workflow.history.model.response.HistoryResponce
import com.hmis_tn.doctor.utils.Utils

class HistoryViewModel(
    application: Application
) : AndroidViewModel(
    application
) {
    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
    }

    fun getHistoryList(
        facility_uuid: Int,
        getHistoryRetrofitCallBack: RetrofitCallback<HistoryResponce>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getHistoryAll(
            BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid
        )?.enqueue(RetrofitMainCallback(getHistoryRetrofitCallBack))
        return
    }
}