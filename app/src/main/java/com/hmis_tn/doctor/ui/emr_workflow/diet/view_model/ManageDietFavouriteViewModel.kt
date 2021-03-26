package com.hmis_tn.doctor.ui.emr_workflow.diet.view_model

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
import com.hmis_tn.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.diet.model.DietFavMangeResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.diet.model.request.RequestDietFavModel
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.InvestigationSearchResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.faveditresponse.FavEditResponse
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.favresponse.FavAddListResponse
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class ManageDietFavouriteViewModel(application: Application) : AndroidViewModel(application) {

    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    init {
        progress.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)

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
            jsonBody.put("paginationSize", 100)
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


    fun getADDFavourite(
        facilityID: Int?,
        requestbody: RequestDietFavModel,
        emrposFavtRetrofitCallback: RetrofitCallback<DietFavMangeResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getDietFavddAll(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!,
            requestbody
        )?.enqueue(RetrofitMainCallback(emrposFavtRetrofitCallback))
        return

    }

    fun getAddListFav(
        facilityUuid: Int,
        favouriteMasterID: Int,
        favouriteTypeID: Int,
        emrposListDataFavtRetrofitCallback: RetrofitCallback<FavAddListResponse>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getFavddAllList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityUuid,
            favouriteMasterID,
            favouriteTypeID
        )?.enqueue(RetrofitMainCallback(emrposListDataFavtRetrofitCallback))
        return

    }


    fun getEditFavourite(
        facilityUuid: Int?,
        testMasterName: String?,
        favouriteId: Int?,
        deparmentUuid: Int?,
        favouriteDisplayOrder: String?,
        isactive: Boolean,
        frequencyId: Int?,
        categoryId: Int?,
        quantity: String?,
        emrposListDataFavtEditRetrofitCallback: RetrofitCallback<FavEditResponse>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        try {
            jsonBody.put("departmentId", deparmentUuid)
            jsonBody.put("favourite_display_order", favouriteDisplayOrder)
            jsonBody.put("diet_master_name", testMasterName)
            jsonBody.put("favourite_id", favouriteId)
            jsonBody.put("is_active", isactive)
            jsonBody.put("diet_frequency_uuid", frequencyId)
            jsonBody.put("diet_category_uuid", categoryId)
            jsonBody.put("quantity", quantity)
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

        apiService?.dietEditFav(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityUuid!!, body
        )?.enqueue(RetrofitMainCallback(emrposListDataFavtEditRetrofitCallback))
        return

    }


    fun deleteFavourite(
        facility_id: Int?,
        favouriteId: Int?,
        deleteRetrofitCallback: RetrofitCallback<DeleteResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("favouriteId", favouriteId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )


        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.deleteRows(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, body
        )?.enqueue(RetrofitMainCallback(deleteRetrofitCallback))
        return
    }
}