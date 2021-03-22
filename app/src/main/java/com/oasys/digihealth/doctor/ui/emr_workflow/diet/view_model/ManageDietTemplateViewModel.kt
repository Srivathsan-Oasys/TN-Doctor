package com.oasys.digihealth.doctor.ui.emr_workflow.diet.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.application.HmisApplication
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitMainCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.template_department.GetAllDepartmentReq
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.model.template_department.GetAllDepartmentResp
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.InvestigationSearchResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.request.RequestTemplateAddDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.response.ReponseTemplateadd
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.updaterequest.UpdateRequestModule
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.updateresponse.UpdateResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempleResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class ManageDietTemplateViewModel(application: Application) : AndroidViewModel(application) {

    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var department_UUID: Int? = 0
    var facility_ID: Int? = 0

    init {
        progress.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        facility_ID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
    }

    fun getAllDepartment(
        facilityID: Int?,
        favAddAllDepartmentCallBack: RetrofitCallback<FavAddAllDepatResponseModel>
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
            userDataStoreBean?.uuid!!, facilityID!!,
            body
        )?.enqueue(RetrofitMainCallback(favAddAllDepartmentCallBack))
        return

    }

    fun getDietCategory(
        facilityID: Int?,
        favCategoryCallBack: RetrofitCallback<FavAddAllDepatResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getDietMasterCategoryList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityID!!
        )?.enqueue(RetrofitMainCallback(favCategoryCallBack))
        return

    }


    fun getDietFrequency(
        facilityID: Int?,
        favFrequencyallBack: RetrofitCallback<FavAddAllDepatResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()


        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getDietMasterFrequency(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityID!!
        )?.enqueue(RetrofitMainCallback(favFrequencyallBack))
        return

    }


    var facilityID = appPreferences?.getInt(AppConstants.FACILITY_UUID)

    fun getDietName(
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

        apiService?.GetDietSearchResult(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityID!!,
            body
        )?.enqueue(RetrofitMainCallback(favAddTestNameCallBack))
        return
    }


    fun dietTemplateDetails(
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
    Update Template
     */


    fun dietUpdateTemplateDetails(
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


    /*
    Template
     */
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
            facility_ID!!,
            AppConstants.FAV_TYPE_ID_DIET
        )?.enqueue(RetrofitMainCallback(templeteRetrofitCallBack))
        return
    }

    fun getDietAllDepartment(
        facility_uuid: Int,
        getAllDepartmentReq: GetAllDepartmentReq,
        getAllDepartmentRespCallback: RetrofitCallback<GetAllDepartmentResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getDietAllDepartment(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            getAllDepartmentReq
        )?.enqueue(RetrofitMainCallback(getAllDepartmentRespCallback))
    }
}