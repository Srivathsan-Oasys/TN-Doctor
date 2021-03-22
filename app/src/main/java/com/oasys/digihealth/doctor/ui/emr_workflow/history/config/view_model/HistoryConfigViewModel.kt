package com.oasys.digihealth.doctor.ui.emr_workflow.history.config.view_model

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
import com.oasys.digihealth.doctor.ui.configuration.model.ConfigResponseModel
import com.oasys.digihealth.doctor.ui.configuration.model.ConfigUpdateResponseModel
import com.oasys.digihealth.doctor.ui.configuration.model.HistoryConfigUpdateRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.config.CriticalCareChartFilterHeadingsResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.config.SaveCriticalCareChartConfig
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.headings.GetCriticalCareChartHeadingsReq
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.headings.GetCriticalCareChartHeadingsResp
import com.oasys.digihealth.doctor.ui.emr_workflow.history.model.response.HistoryResponce
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EmrWorkFlowResponseModel
import com.oasys.digihealth.doctor.ui.login.model.SimpleResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class HistoryConfigViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {

    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    init {
        progress.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
    }

    fun getConfigList(configRetrofitCallBack: RetrofitCallback<ConfigResponseModel>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        val jsonBody = JSONObject()
        try {
            jsonBody.put("context_uuid", AppConstants.History)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getConfigList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            body
        )?.enqueue(RetrofitMainCallback(configRetrofitCallBack))
        return
    }

    fun postRequestParameter(
        facility_id: Int?,
        configRequestData: ArrayList<HistoryConfigUpdateRequestModel?>,
        configFinalRetrofitCallBack: RetrofitCallback<ConfigUpdateResponseModel>,
        configStatus: Boolean?
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        if (configStatus!!) {

            apiService?.getHistoryConfigCreate(
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!,
                configRequestData
            )?.enqueue(RetrofitMainCallback(configFinalRetrofitCallBack))

        } else {

            apiService?.getHistoryConfigUpdate(
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!,
                configRequestData
            )?.enqueue(RetrofitMainCallback(configFinalRetrofitCallBack))

        }
        return

    }

    fun getEmrWorkFlowFav(emrWorkFlowRetrofitCallBack: RetrofitCallback<EmrWorkFlowResponseModel>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getEmrWorkflow(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!
        )!!.enqueue(
            RetrofitMainCallback(emrWorkFlowRetrofitCallBack)
        )
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
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid
        )?.enqueue(RetrofitMainCallback(getHistoryRetrofitCallBack))
        return
    }

    //ccc config

    fun getCriticalCareChartHeadings(
        facility_uuid: Int,
        getCriticalCareChartHeadingsReq: GetCriticalCareChartHeadingsReq,
        getCriticalCareChartHeadingsRespCallback: RetrofitCallback<GetCriticalCareChartHeadingsResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val doctorId = userDataStoreBean?.uuid

        apiService?.getCriticalCareChartHeadings(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            getCriticalCareChartHeadingsReq
        )?.enqueue(RetrofitMainCallback(getCriticalCareChartHeadingsRespCallback))
    }

    fun getCriticalCareChartFilterHeadings(
        facility_uuid: Int,
        getCriticalCareChartHeadingsRespCallback: RetrofitCallback<CriticalCareChartFilterHeadingsResponse>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val doctorId = userDataStoreBean?.uuid

        apiService?.getCriticalCareChartFilterHeadings(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            appPreferences!!.getInt(AppConstants.PATIENT_UUID)
        )?.enqueue(RetrofitMainCallback(getCriticalCareChartHeadingsRespCallback))
    }

    fun updateConfig(
        facilityId: Int?,
        reqestData: ArrayList<SaveCriticalCareChartConfig>,
        updateConfigCallback: RetrofitCallback<SimpleResponseModel>
    ) {


        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val doctorId = userDataStoreBean?.uuid

        apiService?.UpdateCriticalCareChartConfig(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId!!,
            appPreferences!!.getInt(AppConstants.PATIENT_UUID),
            reqestData
        )?.enqueue(RetrofitMainCallback(updateConfigCallback))

    }

}