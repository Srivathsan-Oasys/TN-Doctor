package com.oasys.digihealth.doctor.ui.resultdispatch.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.application.HmisApplication
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.ui.resultdispatch.model.ResponseResultDispatch
import com.oasys.digihealth.doctor.ui.resultdispatch.request.RequestDispatchSearch
import com.oasys.digihealth.doctor.utils.Utils


class ResultDispatchViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {

    fun getresultdispatch(
        requestResultdiapatch: RequestDispatchSearch,
        resultdispatchResponseRetrofitCallback: RetrofitCallback<ResponseResultDispatch>
    ) {
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getresultdispatchlist(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            AppConstants.ACCEPT_LANGUAGE_EN,

            requestResultdiapatch
        ).enqueue(RetrofitMainCallback(resultdispatchResponseRetrofitCallback))
    }

    fun getresultdispatchsecond(
        requestResultdiapatch: RequestDispatchSearch,
        secondresultdispatchResponseRetrofitCallback: RetrofitCallback<ResponseResultDispatch>
    ) {
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getresultdispatchlist(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            AppConstants.ACCEPT_LANGUAGE_EN,

            requestResultdiapatch
        ).enqueue(RetrofitMainCallback(secondresultdispatchResponseRetrofitCallback))
    }


    var enterOTPEditText = MutableLiveData<String>()
    var enterNewPasswordEditText = MutableLiveData<String>()
    var enterConfirmPasswordEditText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var errorText = MutableLiveData<String>()


    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    var facility_id: Int? = 0

    var appPreferences: AppPreferences? = null


    init {

        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)

        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)

        //progress.value = 8
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
    }
}