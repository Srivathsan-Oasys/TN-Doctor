package com.hmis_tn.doctor.ui.quick_reg.view_model

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
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.SendApprovalRequestModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.TestProcessRequestModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.orderRequest.OrderProcessDetailsResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.orderRequest.Req
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.LabAssignedToResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.LabTestSpinnerResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.SimpleResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.UserProfileResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.labtest.response.testprocess.TestProcessResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.testprocess.sampleTransportRequestModel
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject


class LabTestProcessViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {


    var enterOTPEditText = MutableLiveData<String>()
    var enterNewPasswordEditText = MutableLiveData<String>()
    var enterConfirmPasswordEditText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var errorText = MutableLiveData<String>()


    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    var facility_id: Int? = 0

    var appPreferences: AppPreferences? = null


    init {

        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)

        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)

        //progress.value = 8
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
    }


    fun getLabTestProcessList(
        labtestProcessListRequest: TestProcessRequestModel,
        GetLabTestProcessListRetrofitCallback: RetrofitCallback<TestProcessResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getLabTestProcess(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, true,
            labtestProcessListRequest
        )?.enqueue(RetrofitMainCallback(GetLabTestProcessListRetrofitCallback))

    }

    fun getLabTestProcessListSecond(
        labtestProcessListRequest: TestProcessRequestModel,
        GetLabTestProcessListSecondRetrofitCallback: RetrofitCallback<TestProcessResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getLabTestProcess(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, true,
            labtestProcessListRequest
        )?.enqueue(RetrofitMainCallback(GetLabTestProcessListSecondRetrofitCallback))

    }

    fun sampleRecived(
        requestData: sampleTransportRequestModel,
        GetLabTestSampleListRetrofitCallback: RetrofitCallback<SimpleResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0


        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.sampleRecived(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            requestData
        )?.enqueue(RetrofitMainCallback(GetLabTestSampleListRetrofitCallback))

    }

    fun orderProcess(
        GetLabTestSampleListRetrofitCallback: RetrofitCallback<UserProfileResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()

        try {
            jsonBody.put("search", "%%%")
            jsonBody.put("facility_uuid", facility_id)
            jsonBody.put("is_result_approve", true)
            jsonBody.put("sortOrder", "ASC")
            jsonBody.put("usertypeId", 6)
            jsonBody.put("is_active", 1)

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

        apiService?.getUserProfile(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(GetLabTestSampleListRetrofitCallback))

    }

    fun orderDetailsGet(
        req: Req,
        GetLabTestSampleListRetrofitCallback: RetrofitCallback<OrderProcessDetailsResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.orderDetailsGet(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            req
        )?.enqueue(RetrofitMainCallback(GetLabTestSampleListRetrofitCallback))

    }

    fun saveApprovel(
        request: SendApprovalRequestModel,
        GetLabTestSampleListRetrofitCallback: RetrofitCallback<SimpleResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.sendApprovel(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            request
        )?.enqueue(RetrofitMainCallback(GetLabTestSampleListRetrofitCallback))

    }

    fun getTextMethod1(
        facilityId: Int,
        ResponseTestMethodRetrofitCallback: RetrofitCallback<LabTestSpinnerResponseModel>
    ) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {

            jsonBody.put("pageNo", 0)

            jsonBody.put("paginationSize", 100000)
            jsonBody.put("sortField", "uuid")
            jsonBody.put("sortOrder", "DESC")
            jsonBody.put("search", "")

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

        apiService?.getLabTestSpinner(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityId, true,
            body
        )?.enqueue(RetrofitMainCallback(ResponseTestMethodRetrofitCallback))

    }

    fun getTextAssignedTo(
        facilityId: Int,
        ResponseTestAssignedToRetrofitCallback: RetrofitCallback<LabAssignedToResponseModel>
    ) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {

            jsonBody.put("sortOrder", "ASC")
            jsonBody.put("sortField", "uuid")
            jsonBody.put("status", "1")
            jsonBody.put("pageNo", "0")
            jsonBody.put("paginationSize", 100)
            jsonBody.put("search", "")


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

        apiService?.getLabAssignedToSpinner(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityId, true,
            body
        )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

    }

}