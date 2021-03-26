package com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog

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
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.response.SnomedChildDataResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.response.SnomedDataResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.response.SnomedParentDataResponseModel
import com.hmis_tn.doctor.utils.Utils

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