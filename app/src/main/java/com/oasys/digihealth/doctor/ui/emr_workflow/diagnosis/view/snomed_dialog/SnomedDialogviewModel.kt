package com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog

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
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.response.SnomedChildDataResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.response.SnomedDataResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.response.SnomedParentDataResponseModel
import com.oasys.digihealth.doctor.utils.Utils

class SnomedDialogviewModel(
    application: Application
) : AndroidViewModel(
    application
) {

    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var appPreferences: AppPreferences? = null

    var facilityID: Int? = 0


    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)

        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)

        facilityID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
    }

    fun searchSnoomed(
        query: String,
        searchsn0omedRetrofitCallback: RetrofitCallback<SnomedDataResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()


        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getSnommed(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityID!!,
            query
        )?.enqueue(RetrofitMainCallback(searchsn0omedRetrofitCallback))
        return

    }


    fun searchParentSnoomed(
        query: String,
        searchsn0omedRetrofitCallback: RetrofitCallback<SnomedParentDataResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getParentSnommed(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityID!!,
            query
        )?.enqueue(RetrofitMainCallback(searchsn0omedRetrofitCallback))
        return

    }

    fun searchChildSnoomed(
        query: String,
        searchsn0omedRetrofitCallback: RetrofitCallback<SnomedChildDataResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getChildSnommed(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityID!!,
            query
        )?.enqueue(RetrofitMainCallback(searchsn0omedRetrofitCallback))
        return

    }
}