package com.hmis_tn.doctor.ui.emr_workflow.lab.view_model

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
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.FavAddResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.labviewresponse.LabViewResponseModule
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject


class ManageLabPrevLabViewModel(
    application: Application?


) : AndroidViewModel(
    application!!
) {

    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var department_UUID: Int? = 0

    init {
        progress.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
    }

    var facilityID = appPreferences?.getInt(AppConstants.FACILITY_UUID)

    fun getDepartmentList(
        facilityID: Int?,
        FavdepartmentRetrofitCallBack: RetrofitCallback<FavAddResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("uuid", department_UUID)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getFavDepartmentList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityID!!,
            body
        )?.enqueue(RetrofitMainCallback(FavdepartmentRetrofitCallBack))
        return
    }

    fun getLabViewDetails(
        PatientOrderUUID: Int,
        labViewListRetrofitCallBack: RetrofitCallback<LabViewResponseModule>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getLabViewDetails(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!, userDataStoreBean.uuid,
            PatientOrderUUID
        )!!.enqueue(
            RetrofitMainCallback(labViewListRetrofitCallBack)
        )


    }


}