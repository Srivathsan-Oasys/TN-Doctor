/*
package com.hmis_tn.doctor.ui.emr_workflow.lab.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.config.AppConstants

import com.hmis_tn.doctor.config.AppConstants.Companion.FAV_TYPE_ID_CHIEF_COMPLAINTS
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.duration.DurationResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.search_complaint.ComplaintSearchResponseModel
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.utils.Utils

class LabViewModelFavourite(
    application: Application) : AndroidViewModel(
    application
) {
    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
    }

    fun getFavourites(emrWorkFlowRetrofitCallBack: RetrofitCallback<FavouritesResponseModel>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getFavourites(AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, userDataStoreBean?.uuid!!, "null", FAV_TYPE_ID_CHIEF_COMPLAINTS)?.enqueue(RetrofitMainCallback(emrWorkFlowRetrofitCallBack))
        return
    }

    fun getDuration(getDurationRetrofitCallBack: RetrofitCallback<DurationResponseModel>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getDuration(AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, userDataStoreBean?.uuid!!)?.enqueue(RetrofitMainCallback(getDurationRetrofitCallBack))
        return
    }


    fun getComplaintSearchResult(
        query: String,
        complaintSearchRetrofitCallBack: RetrofitCallback<ComplaintSearchResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getChiefComplaintsSearchResult(AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, userDataStoreBean?.uuid!!, "filterbythree", query)?.enqueue(RetrofitMainCallback(complaintSearchRetrofitCallBack))
        return
    }

}*/
