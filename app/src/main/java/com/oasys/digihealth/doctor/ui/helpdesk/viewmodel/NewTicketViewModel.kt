package com.oasys.digihealth.doctor.ui.helpdesk.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.application.HmisApplication
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.ui.helpdesk.model.*
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject


class NewTicketViewModel(
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

    fun getInstitution(
        facilityId: Int,
        ResponseDistrictRetrofitCallback: RetrofitCallback<TicketInstitutionResponseModel>
    ) {

        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {

            jsonBody.put("uuid", facility_id)
            jsonBody.put("Id", facility_id)

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

        apiService?.getFacilityByUUID(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
            body
        ).enqueue(RetrofitMainCallback(ResponseDistrictRetrofitCallback))

    }

    fun getAssetCode(
        jsonBody: JSONObject,
        ResponseDistrictRetrofitCallback: RetrofitCallback<AssetResponseModel>
    ) {

        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getAssetCode(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
            body
        ).enqueue(RetrofitMainCallback(ResponseDistrictRetrofitCallback))

    }

    fun getDepartment(
        facilityId: Int,
        ResponseDistrictRetrofitCallback: RetrofitCallback<DepartmentResponseModel>
    ) {

        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {

            jsonBody.put("facility_uuid", facilityId)

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

        apiService?.getDepartment(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
            body
        ).enqueue(RetrofitMainCallback(ResponseDistrictRetrofitCallback))

    }

    fun addNewTicket(
        requestModel: AddTicketRequestModel,
        body: MultipartBody.Part,
        ResponseDistrictRetrofitCallback: RetrofitCallback<TicketInstitutionResponseModel>
    ) {

        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()

        val multipartMobile = RequestBody.create(
            MultipartBody.FORM, requestModel.mobile!!
        )
        val multipartSubject = RequestBody.create(
            MultipartBody.FORM, requestModel.subject!!
        )
        val multipartCreatedBy = RequestBody.create(
            MultipartBody.FORM, requestModel.created_by!!.toString()
        )
        val multipartUserUUID = RequestBody.create(
            MultipartBody.FORM, requestModel.application_user_uuid!!.toString()
        )
        val multipartAssetCode = RequestBody.create(
            MultipartBody.FORM, requestModel.asset_code!!
        )
        val multipartAssignTo = RequestBody.create(
            MultipartBody.FORM, requestModel.assignto!!.toString()
        )
        val multipartMake = RequestBody.create(
            MultipartBody.FORM, requestModel.make!!
        )
        val multipartModel = RequestBody.create(
            MultipartBody.FORM, requestModel.model!!
        )
        val multipartDescription = RequestBody.create(
            MultipartBody.FORM, requestModel.problem_description!!
        )
        val multipartSerial = RequestBody.create(
            MultipartBody.FORM, requestModel.serial!!
        )
        val multipartVendor = RequestBody.create(
            MultipartBody.FORM, requestModel.vendorMail!!
        )
        val multipartFacility = RequestBody.create(
            MultipartBody.FORM, requestModel.facility_uuid!!.toString()
        )
        val multipartDepartment = RequestBody.create(
            MultipartBody.FORM, requestModel.department_uuid!!.toString()
        )
        val multipartUserType = RequestBody.create(
            MultipartBody.FORM, requestModel.user_type_uuid!!.toString()
        )
        val multipartTicketStatus = RequestBody.create(
            MultipartBody.FORM, requestModel.ticketstatus_uuid!!.toString()
        )
        val multipartAssetUUID = RequestBody.create(
            MultipartBody.FORM, requestModel.assest_uuid!!.toString()
        )
        val multipartCategory = RequestBody.create(
            MultipartBody.FORM, requestModel.category_uuid!!.toString()
        )

        val multipartPriority = RequestBody.create(
            MultipartBody.FORM, requestModel.priority_uuid!!.toString()
        )

        apiService?.addNewTicket(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name, body,
            multipartMobile, multipartSubject, multipartCreatedBy, multipartUserUUID,
            multipartAssetCode, multipartAssignTo, multipartMake, multipartModel,
            multipartDescription, multipartSerial, multipartVendor, multipartFacility,
            multipartDepartment, multipartUserType, multipartTicketStatus, multipartAssetUUID,
            multipartCategory, multipartPriority, multipartPriority
        ).enqueue(RetrofitMainCallback(ResponseDistrictRetrofitCallback)) // requestModel!!

    }


    fun getTicketById(
        jsonBody: JSONObject,
        GetLabTestApprovalListRetrofitCallback: RetrofitCallback<TicketIdResponseModel>
    ) {

        Log.e("getTicketList", "inside")
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()


        apiService?.getTicketById(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
            body
        ).enqueue(RetrofitMainCallback(GetLabTestApprovalListRetrofitCallback))

    }

    fun getCategoryList(
        jsonBody: JSONObject,
        ResponseDistrictRetrofitCallback: RetrofitCallback<CategoryListResponseModel>
    ) {

        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
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
        ).enqueue(RetrofitMainCallback(ResponseDistrictRetrofitCallback))

    }

    fun getPriorityList(
        jsonBody: JSONObject,
        ResponseDistrictRetrofitCallback: RetrofitCallback<CategoryListResponseModel>
    ) {

        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
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
        ).enqueue(RetrofitMainCallback(ResponseDistrictRetrofitCallback))

    }

    fun getAssetData(
        datas: String,
        facilityId: Int,
        selectedDepartmentid: Int,
        assetResponseRetrofitCallback: RetrofitCallback<AssetResponseModel>
    ) {


        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {
            jsonBody.put("codename", datas)
            jsonBody.put("facility_uuid", facilityId)
            jsonBody.put("department_uuid", selectedDepartmentid)
            jsonBody.put("pageNo", 0)
            jsonBody.put("paginationSize", 100)


        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        Log.e("Req", jsonBody.toString())

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getAssetCode(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
            body
        ).enqueue(RetrofitMainCallback(assetResponseRetrofitCallback))


    }

}