package com.oasys.digihealth.doctor.ui.emr_workflow.lab.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.application.HmisApplication
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.faveditresponse.FavEditResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse.FavAddListResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.request.RequestLabFavModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject


class ManageLabFavourteViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {

    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var department_UUID: Int? = 0
    var facilityID: Int? = 0

    init {
        progress.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        facilityID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
    }


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
        ).enqueue(RetrofitMainCallback(FavdepartmentRetrofitCallBack))
        return
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
            jsonBody.put("pageNo", 0)
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
            userDataStoreBean?.uuid!!, facilityID!!,
            body
        ).enqueue(RetrofitMainCallback(favAddAllDepartmentCallBack))
        return

    }

    fun getTestName(
        name: String,
        favAddTestNameCallBack: RetrofitCallback<FavAddTestNameResponse>
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

        apiService?.getAutocommitText(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityID!!,
            body
        ).enqueue(RetrofitMainCallback(favAddTestNameCallBack))
        return
    }

    fun getADDFavourite(
        facilityID: Int?, requestbody: RequestLabFavModel,
        emrposFavtRetrofitCallback: RetrofitCallback<LabFavManageResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getFavddAll(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityID!!, requestbody
        ).enqueue(RetrofitMainCallback(emrposFavtRetrofitCallback))
        return

    }

    fun getAddListFav(
        facilityUuid: Int?,
        favouriteMasterID: Int?,
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
            userDataStoreBean?.uuid!!, facilityUuid!!, favouriteMasterID!!, 0
        ).enqueue(RetrofitMainCallback(emrposListDataFavtRetrofitCallback))
        return

    }

    fun getEditFavourite(
        facilityUuid: Int?,
        testMasterName: String?,
        favouriteId: Int?,
        deparmentUuid: Int?,
        favouriteDisplayOrder: String?,
        isactive: Boolean,
        emrposListDataFavtEditRetrofitCallback: RetrofitCallback<FavEditResponse>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        try {
            jsonBody.put("department_uuid", deparmentUuid)
            jsonBody.put("favourite_display_order", favouriteDisplayOrder)
            jsonBody.put("testname", testMasterName)
            jsonBody.put("favourite_id", favouriteId)
            jsonBody.put("is_active", isactive)
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

        apiService?.labEditFav(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityUuid!!, body
        ).enqueue(RetrofitMainCallback(emrposListDataFavtEditRetrofitCallback))
        return


    }

    /*
    Delete
     */
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
        ).enqueue(RetrofitMainCallback(deleteRetrofitCallback))
        return
    }

    fun getFavourites(
        emrWorkFlowRetrofitCallBack: RetrofitCallback<FavouritesResponseModel>,
        departmentID: Int?
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getFavourites(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityID!!,
            departmentID!!,
            AppConstants.FAV_TYPE_ID_LAB
        ).enqueue(RetrofitMainCallback(emrWorkFlowRetrofitCallBack))
        return
    }


}