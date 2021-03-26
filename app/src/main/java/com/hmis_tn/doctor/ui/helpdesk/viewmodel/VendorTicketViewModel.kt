package com.hmis_tn.doctor.ui.helpdesk.viewmodel

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
import com.hmis_tn.doctor.ui.helpdesk.model.AddTicketRequestModel
import com.hmis_tn.doctor.ui.helpdesk.model.CategoryListResponseModel
import com.hmis_tn.doctor.ui.helpdesk.model.VendorByMobileResponseModel
import com.hmis_tn.doctor.ui.helpdesk.model.VendorListResponseModel
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class VendorTicketViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {

    var progress = MutableLiveData<Int>()
    var errorText = MutableLiveData<String>()

    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    var facility_id: Int? = 0

    var appPreferences: AppPreferences? = null

    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
    }

    fun getCategoryList(ResponseDistrictRetrofitCallback: RetrofitCallback<CategoryListResponseModel>) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("table_name", "help_desk_category")

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

        apiService?.getCategory(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
            body
        )?.enqueue(RetrofitMainCallback(ResponseDistrictRetrofitCallback))

    }

    fun getPriorityList(ResponseDistrictRetrofitCallback: RetrofitCallback<CategoryListResponseModel>) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("table_name", "priority")
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

        apiService?.getCategory(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
            body
        )?.enqueue(RetrofitMainCallback(ResponseDistrictRetrofitCallback))

    }

    fun getSubCategoryList(
        category_uuid: Int,
        ResponseDistrictRetrofitCallback: RetrofitCallback<CategoryListResponseModel>
    ) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("table_name", "sub_category")
            jsonBody.put("column_name", "category_uuid")
            jsonBody.put("category_uuid", category_uuid)
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

        apiService?.getCategory(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
            body
        )?.enqueue(RetrofitMainCallback(ResponseDistrictRetrofitCallback))

    }

    fun getVendorList(
        vendor_name: String,
        ResponseDistrictRetrofitCallback: RetrofitCallback<VendorListResponseModel>
    ) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("pageNo", 0)
            jsonBody.put("paginationSize", 10)
            jsonBody.put("sortField", "uuid")
            jsonBody.put("sortOrder", "ASC")
            jsonBody.put("vendor_name", vendor_name)
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

        apiService?.getVendor(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
            body
        )?.enqueue(RetrofitMainCallback(ResponseDistrictRetrofitCallback))

    }

    fun getVendorByMobile(
        mobile: String,
        ResponseDistrictRetrofitCallback: RetrofitCallback<VendorByMobileResponseModel>
    ) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("mobile", mobile)
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

        apiService?.getVendorByMobile(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
            body
        )?.enqueue(RetrofitMainCallback(ResponseDistrictRetrofitCallback))

    }

    fun saveVendor(
        requestModel: AddTicketRequestModel,
        ResponseDistrictRetrofitCallback: RetrofitCallback<VendorByMobileResponseModel>
    ) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getSaveVendor(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
            requestModel
        )?.enqueue(RetrofitMainCallback(ResponseDistrictRetrofitCallback))

    }

}