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
import com.hmis_tn.doctor.ui.quick_reg.model.reports.requset.LabConsolidatedReportRequestModel
import com.hmis_tn.doctor.ui.quick_reg.model.reports.response.LabConsolidatedReportLabelResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.reports.response.LabConsolidatedReportResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.reports.response.LabFilterResponseModel
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class LabConsolidatedReportViewModel(
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


    fun getLabConsolidatedReportList(
        toggle: String,
        requestLabConsolidatedReportListRequest: LabConsolidatedReportRequestModel,
        GetLabTestApprovalListRetrofitCallback: RetrofitCallback<LabConsolidatedReportResponseModel>
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
            apiService?.getLabConsolidatedOPReportTable(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                requestLabConsolidatedReportListRequest
            )?.enqueue(RetrofitMainCallback(GetLabTestApprovalListRetrofitCallback))
        } else {
            apiService?.getLabConsolidatedIPReportTable(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                requestLabConsolidatedReportListRequest
            )?.enqueue(RetrofitMainCallback(GetLabTestApprovalListRetrofitCallback))

        }

    }

    fun getLabConsolidatedReportLabel(
        toggle: String,
        requestLabConsolidatedReportListRequest: LabConsolidatedReportRequestModel,
        GetLabTestApprovalListRetrofitCallback: RetrofitCallback<LabConsolidatedReportLabelResponseModel>
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
            apiService?.getLabConsolidatedOPReportLabel(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                requestLabConsolidatedReportListRequest
            )?.enqueue(RetrofitMainCallback(GetLabTestApprovalListRetrofitCallback))

        } else {
            apiService?.getLabConsolidatedIPReportLabel(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                requestLabConsolidatedReportListRequest
            )?.enqueue(RetrofitMainCallback(GetLabTestApprovalListRetrofitCallback))

        }

    }


    fun getLabConsolidatedReportListSecond(
        toggle: String,
        labConsolidatedReportRequestModel: LabConsolidatedReportRequestModel,
        labTestApprovalResponseSecondRetrofitCallback: RetrofitCallback<LabConsolidatedReportResponseModel>
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
            apiService?.getLabConsolidatedOPReportTable(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                labConsolidatedReportRequestModel
            )?.enqueue(RetrofitMainCallback(labTestApprovalResponseSecondRetrofitCallback))

        } else {
            apiService?.getLabConsolidatedIPReportTable(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                labConsolidatedReportRequestModel
            )?.enqueue(RetrofitMainCallback(labTestApprovalResponseSecondRetrofitCallback))
        }


    }

    fun getDistrict(
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
            apiService?.getLabDistrictOPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseDistrictRetrofitCallback))

        } else {
            apiService?.getLabDistrictIPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseDistrictRetrofitCallback))

        }

    }


    fun getHUD(
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
            apiService?.getLabHUDOPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        } else {
            apiService?.getLabHUDIPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }

    fun getBlock(
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
            apiService?.getLabBlockOPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        } else {
            apiService?.getLabBlockIPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }

    fun getOffice(
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
            apiService?.getLabOfficeOPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        } else {
            apiService?.getLabOfficeIPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }

    fun getInstitutionType(
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
            apiService?.getLabInstitutionTypeOPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        } else {
            apiService?.getLabInstitutionTypeIPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }

    fun getDepartment(
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
            apiService?.getLabDepartmentOPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        } else {
            apiService?.getLabDepartmentIPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }


    fun getInstitution(
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
            apiService?.getLabInstitutionOPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        } else {
            apiService?.getLabInstitutionIPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }

    fun getTestName(
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
            apiService?.getLabTestNameOPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        } else {
            apiService?.getLabTestNameIPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }

    fun getLabName(
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
            apiService?.getLabNameOPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        } else {
            apiService?.getLabNameIPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }

    fun getGender(
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
            apiService?.getLabGenderOPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))
        } else {
            apiService?.getLabGenderIPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))
        }


    }

    fun getStatus(
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
            apiService?.getLabStatusOPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))
        } else {
            apiService?.getLabStatusIPSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, userDataStoreBean.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))
        }


    }

}