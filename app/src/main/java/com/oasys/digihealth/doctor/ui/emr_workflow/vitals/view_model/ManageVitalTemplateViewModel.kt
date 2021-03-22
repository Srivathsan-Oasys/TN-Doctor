package com.oasys.digihealth.doctor.ui.emr_workflow.vitals.view_model

//import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.VitalsSearchResponseModel
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
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.request.RequestTemplateAddDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.response.ReponseTemplateadd
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.updateresponse.UpdateResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempleResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.request.VitalFavUpdateRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.response.VitalSearchNameResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject


class ManageVitalTemplateViewModel(
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

    fun getVitalsSearch(
        query: String,
        faciltyID: Int?,
        vitalsSearchRetrofitCallBack: RetrofitCallback<VitalSearchNameResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("pageNo", 0)
            jsonBody.put("paginationSize", 10)
            jsonBody.put("searchValue", query)
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
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getVitalSearchNameNew(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, userDataStoreBean?.uuid!!,
            faciltyID!!, body
        )?.enqueue(RetrofitMainCallback(vitalsSearchRetrofitCallBack))
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
            AppConstants.FAV_TYPE_ID_LAB
        )?.enqueue(RetrofitMainCallback(templeteRetrofitCallBack))
        return
    }

    fun labUpdateTemplateDetails(
        facilityUuid: Int?,
        requestTemplateUpdateDetails: VitalFavUpdateRequestModel,
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
        apiService?.getVitalTemplateUpdate(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityUuid!!,
            requestTemplateUpdateDetails
        )?.enqueue(RetrofitMainCallback(UpdateemrlabTemplateAddRetrofitCallback))
        return


    }


}