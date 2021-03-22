package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.view_model

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
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.InvestigationSearchResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.request.RequestTemplateAddDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.response.ReponseTemplateadd
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.updaterequest.UpdateRequestModule
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.updateresponse.UpdateResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempleResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject


class ManageInvestTemplateViewModel(
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


    fun getRadiologyDepartmentList(
        facilityID: Int?,
        templateRadiodepartmentRetrofitCallBack: RetrofitCallback<FavAddResponseModel>
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
        )?.enqueue(RetrofitMainCallback(templateRadiodepartmentRetrofitCallBack))
        return
    }

    fun getRadiologyAllDepartment(
        facilityID: Int?,
        tempRadiologyAddAllDepartmentCallBack: RetrofitCallback<FavAddAllDepatResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        try {
            jsonBody.put("facilityBased", true)
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

        apiService?.getFavddAllADepartmentList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!,
            body
        )?.enqueue(RetrofitMainCallback(tempRadiologyAddAllDepartmentCallBack))
        return
    }

    fun getTestName(
        name: String,
        favAddTestNameCallBack: RetrofitCallback<InvestigationSearchResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        try {
            jsonBody.put("search", name)
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

        apiService?.getInvestigationSearch(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!,
            body
        )?.enqueue(RetrofitMainCallback(favAddTestNameCallBack))
        return
    }

    /*
   Update Template
    */

    fun labUpdateTemplateDetails(
        facilityUuid: Int?,
        requestTemplateUpdateDetails: UpdateRequestModule,
        UpdateemrlabTemplateAddRetrofitCallback: RetrofitCallback<UpdateResponse>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.getTemplateUpdate(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityUuid!!,
            requestTemplateUpdateDetails
        )?.enqueue(RetrofitMainCallback(UpdateemrlabTemplateAddRetrofitCallback))
        return
    }

    fun labTemplateDetails(
        facilityUuid: Int?,
        requestTemplateAddDetails: RequestTemplateAddDetails,
        emrlabTemplateAddRetrofitCallback: RetrofitCallback<ReponseTemplateadd>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.createTemplate(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityUuid!!,
            requestTemplateAddDetails
        )?.enqueue(RetrofitMainCallback(emrlabTemplateAddRetrofitCallback))
        return
    }

    /*
  Templete
   */

    fun getTemplete(templeteRetrofitCallBack: RetrofitCallback<TempleResponseModel>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getTemplete(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            department_UUID!!,
            facilityID!!,
            AppConstants.FAV_TYPE_ID_Vitual
        )?.enqueue(RetrofitMainCallback(templeteRetrofitCallBack))
        return
    }
}