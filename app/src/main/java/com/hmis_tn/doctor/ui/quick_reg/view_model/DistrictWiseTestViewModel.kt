package com.hmis_tn.doctor.ui.quick_reg.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitMainCallback
import com.hmis_tn.doctor.ui.quick_reg.model.reports.requset.LabWiseReportRequestModel
import com.hmis_tn.doctor.ui.quick_reg.model.reports.response.LabFilterResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.reports.response.LabTestWiseReportResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.reports.response.LabWiseReportLabelResponseModel
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class DistrictWiseTestViewModel(
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

        //progress.value = 8
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
    }

    fun getDistrictReportListSecond(
        toggle: String,
        labWiseReportRequestModel: LabWiseReportRequestModel,
        labWiseResponseSecondRetrofitCallback: RetrofitCallback<LabTestWiseReportResponseModel>
    ) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        if (toggle.equals("OP")) {
            apiService?.getDistrictTestCountTableList(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                labWiseReportRequestModel
            )?.enqueue(RetrofitMainCallback(labWiseResponseSecondRetrofitCallback))

        } else {
            apiService?.getDistrictTestCountTableListIp(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                labWiseReportRequestModel
            )?.enqueue(RetrofitMainCallback(labWiseResponseSecondRetrofitCallback))

        }


    }

    fun getDistrictWiseTestTableList(
        toggle: String,
        requestLabWiseReportListRequest: LabWiseReportRequestModel,
        GetLabWiseListRetrofitCallback: RetrofitCallback<LabTestWiseReportResponseModel>
    ) {

        Log.e("getList fun", "inside")
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        if (toggle.equals("OP")) {
            apiService?.getDistrictTestCountTableList(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                requestLabWiseReportListRequest
            )?.enqueue(RetrofitMainCallback(GetLabWiseListRetrofitCallback))

        } else {
            apiService?.getDistrictTestCountTableListIp(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                requestLabWiseReportListRequest
            )?.enqueue(RetrofitMainCallback(GetLabWiseListRetrofitCallback))

        }

    }


    fun getDistrictWiseTestReportLabel(
        toggle: String,
        requestLabWiseReportListRequest: LabWiseReportRequestModel,
        GetLabWiseListRetrofitCallback: RetrofitCallback<LabWiseReportLabelResponseModel>
    ) {

        Log.e("getList fun", "inside")
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        if (toggle.equals("OP")) {
            apiService?.getDistrictTestCountLabelList(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                requestLabWiseReportListRequest
            )?.enqueue(RetrofitMainCallback(GetLabWiseListRetrofitCallback))

        } else {
            apiService?.getDistrictTestCountLabelListIp(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                requestLabWiseReportListRequest
            )?.enqueue(RetrofitMainCallback(GetLabWiseListRetrofitCallback))

        }

    }


    fun getDistrictWiseDropDownTest(
        toggle: String,
        ResponseDistrictRetrofitCallback: RetrofitCallback<LabFilterResponseModel>
    ) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {

            jsonBody.put("user_Id", userDataStoreBean?.uuid!!)

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

        if (toggle.equals("OP")) {
            apiService?.getDistTestDropDownList(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseDistrictRetrofitCallback))

        } else {
            apiService?.getDistTestDropDownListIp(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseDistrictRetrofitCallback))

        }

    }


    fun getDistrictWiseTestHUD(
        toggle: String,
        district_id: MutableList<Int>,
        ResponseTestAssignedToRetrofitCallback: RetrofitCallback<LabFilterResponseModel>
    ) {

        val districtID = JSONArray()
        for (item in district_id) {
            districtID.put(item)
        }

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }


        try {

            jsonBody.put("user_Id", userDataStoreBean?.uuid!!)
            jsonBody.putOpt("district_Id", districtID)

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

        if (toggle.equals("OP")) {
            apiService?.getDistTestHUDList(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        } else {
            apiService?.getDistTestHUDListIp(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }

    fun getDistrictWiseTestBlock(
        toggle: String,
        district_id: MutableList<Int>,
        hud_id: MutableList<Int>,
        ResponseTestAssignedToRetrofitCallback: RetrofitCallback<LabFilterResponseModel>
    ) {

        val districtID = JSONArray()
        for (item in district_id) {
            districtID.put(item)
        }
        val hudID = JSONArray()
        for (item in hud_id) {
            hudID.put(item)
        }

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {

            jsonBody.put("user_Id", userDataStoreBean?.uuid!!)
            jsonBody.put("district_Id", districtID)
            jsonBody.put("hud_Id", hudID)

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

        if (toggle.equals("OP")) {
            apiService?.getDistTestBlockList(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        } else {
            apiService?.getDistTestBlockListIp(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))


        }
    }

    fun getDistrictWiseTestOffice(
        toggle: String,
        district_id: MutableList<Int>,
        hud_id: MutableList<Int>,
        block_id: MutableList<Int>,
        ResponseTestAssignedToRetrofitCallback: RetrofitCallback<LabFilterResponseModel>
    ) {

        val districtID = JSONArray()
        for (item in district_id) {
            districtID.put(item)
        }
        val hudID = JSONArray()
        for (item in hud_id) {
            hudID.put(item)
        }
        val blockID = JSONArray()
        for (item in block_id) {
            blockID.put(item)
        }

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {

            jsonBody.put("user_Id", userDataStoreBean?.uuid!!)
            jsonBody.put("district_Id", districtID)
            jsonBody.put("hud_Id", hudID)
            jsonBody.put("block_Id", blockID)

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

        if (toggle.equals("OP")) {
            apiService?.getDistTestOfficeList(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        } else {
            apiService?.getDistTestOfficeListIp(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }

    fun getDistrictWiseTestInstitutionType(
        toggle: String,
        district_id: MutableList<Int>,
        hud_id: MutableList<Int>,
        block_id: MutableList<Int>,
        office_id: MutableList<Int>,
        ResponseTestAssignedToRetrofitCallback: RetrofitCallback<LabFilterResponseModel>
    ) {

        val districtID = JSONArray()
        for (item in district_id) {
            districtID.put(item)
        }
        val hudID = JSONArray()
        for (item in hud_id) {
            hudID.put(item)
        }
        val blockID = JSONArray()
        for (item in block_id) {
            blockID.put(item)
        }
        val officeID = JSONArray()
        for (item in office_id) {
            officeID.put(item)
        }

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {

            jsonBody.put("user_Id", userDataStoreBean?.uuid!!)
            jsonBody.put("district_Id", districtID)
            jsonBody.put("hud_Id", hudID)
            jsonBody.put("block_Id", blockID)
            jsonBody.put("office_Id", officeID)

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

        if (toggle.equals("OP")) {
            apiService?.getDistTestInstituteTypeList(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        } else {
            apiService?.getDistTestInstituteTypeListIp(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }


    }


    fun getDistrictWiseTestInstitution(
        toggle: String,
        district_id: MutableList<Int>,
        hud_id: MutableList<Int>,
        block_id: MutableList<Int>,
        office_id: MutableList<Int>,
        institutionType_id: MutableList<Int>,
        ResponseTestAssignedToRetrofitCallback: RetrofitCallback<LabFilterResponseModel>
    ) {

        val districtID = JSONArray()
        for (item in district_id) {
            districtID.put(item)
        }
        val hudID = JSONArray()
        for (item in hud_id) {
            hudID.put(item)
        }
        val blockID = JSONArray()
        for (item in block_id) {
            blockID.put(item)
        }
        val officeID = JSONArray()
        for (item in office_id) {
            officeID.put(item)
        }

        val institutionTypeID = JSONArray()
        for (item in institutionType_id) {
            institutionTypeID.put(item)
        }

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {

            jsonBody.put("user_Id", userDataStoreBean?.uuid!!)
            jsonBody.put("district_Id", districtID)
            jsonBody.put("hud_Id", hudID)
            jsonBody.put("block_Id", blockID)
            jsonBody.put("office_Id", officeID)
            jsonBody.put("institutiontype_Id", institutionTypeID)

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

        if (toggle.equals("OP")) {
            apiService?.getDistTestInstituteList(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        } else {
            apiService?.getDistTestInstituteListIp(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }

    fun getDistrictWiseTestName(
        toggle: String,
        institutionId: MutableList<Int>,
        ResponseTestAssignedToRetrofitCallback: RetrofitCallback<LabFilterResponseModel>
    ) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {
            val institution = JSONArray()
            for (item in institutionId) {
                institution.put(item)
            }
            jsonBody.put("user_Id", userDataStoreBean?.uuid!!)
            jsonBody.put("institution_Id", institution)

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

        if (toggle.equals("OP")) {
            apiService?.getDistrictTestNameList(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        } else {
            apiService?.getDistrictTestNameListIp(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }

    fun getDistrictWiseLabName(
        toggle: String,
        institutionId: MutableList<Int>,
        ResponseTestAssignedToRetrofitCallback: RetrofitCallback<LabFilterResponseModel>
    ) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {
            val institution = JSONArray()
            for (item in institutionId) {
                institution.put(item)
            }
            jsonBody.put("user_Id", userDataStoreBean?.uuid!!)
            jsonBody.put("institution_Id", institution)

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

        if (toggle.equals("OP")) {
            apiService?.getDistTestLabNameList(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        } else {
            apiService?.getDistTestLabNameListIp(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }

    fun getDistrictWiseTestGender(
        toggle: String,
        ResponseTestAssignedToRetrofitCallback: RetrofitCallback<LabFilterResponseModel>
    ) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {

            jsonBody.put("user_Id", userDataStoreBean?.uuid!!)

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

        if (toggle.equals("OP")) {
            apiService?.getDistTestGenderList(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        } else {
            apiService?.getDistTestGenderListIp(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }

    fun getDistrictWiseTestStatus(
        toggle: String,
        ResponseTestAssignedToRetrofitCallback: RetrofitCallback<LabFilterResponseModel>
    ) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {

            jsonBody.put("user_Id", userDataStoreBean?.uuid!!)

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

        if (toggle.equals("OP")) {
            apiService?.getDistTestOrderStatusList(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        } else {
            apiService?.getDistTestOrderStatusListIp(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }
}