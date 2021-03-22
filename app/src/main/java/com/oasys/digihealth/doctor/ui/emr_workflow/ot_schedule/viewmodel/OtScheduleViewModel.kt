package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.viewmodel

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
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.response.OtNameSpinnerResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.response.OtSchedulToCalendarResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.response.OtSurgeryNameResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.response.OtTypeResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class OtScheduleViewModel(
    application: Application
) : AndroidViewModel(
    application
) {
    var progressBar = MutableLiveData<Int>()
    var errorText = MutableLiveData<String>()
    private var noInternetLayout: MutableLiveData<Int>? = null
    var errorTextVisibility = MutableLiveData<Int>()

    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var appPreferences: AppPreferences? = null


    init {
        errorTextVisibility.value = 8
        progressBar.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
    }

    fun getSurgeryName(
        facilityid: Int?,
        dept_id: Int?,
        surgeryRetrofitCallback: RetrofitCallback<OtSurgeryNameResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("facility_uuid", facilityid)
            jsonBody.put("department_uuid", dept_id)
            jsonBody.put("searchkey", "")
            jsonBody.put("pageNo", 0)
            jsonBody.put("paginationSize", 100)
            jsonBody.put("sortField", "p_name")
            jsonBody.put("sortOrder", "ASC")


            /*{"facility_uuid":"3","department_uuid":"2","searchkey":"","pageNo":0,"paginationSize":100,"sortField":"p_name","sortOrder":"ASC"}*/

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progressBar.value = 0
        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()
        apiService?.getOtSurgery(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(surgeryRetrofitCallback))
    }


    fun getOtName(
        facilityid: Int?,
        otTypeRetrofitCallback: RetrofitCallback<OtNameSpinnerResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("pageNo", 0)
            jsonBody.put("paginationSize", 10)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progressBar.value = 0
        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()
        apiService?.getOtName(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(otTypeRetrofitCallback))
    }

    fun getOtType(facilityid: Int?, otTypeRetrofitCallback: RetrofitCallback<OtTypeResponseModel>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("table_name", "ot_types")
            jsonBody.put("sortField", "name")

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progressBar.value = 0
        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()
        apiService?.getOtType(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(otTypeRetrofitCallback))
    }


    fun getOtScheduleList(
        facilityid: Int?,
        calDate: String,
        surgeryNameId: String,
        otNameId: String,
        otTypeId: String,
        otTypeRetrofitCallback: RetrofitCallback<OtSchedulToCalendarResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("calender_date", calDate)
            jsonBody.put("calender_type", 3)
            jsonBody.put("facility_uuid", "")
            jsonBody.put("department_uuid", "")
            jsonBody.put("surgeryId", surgeryNameId)
            jsonBody.put("otTypeId", otTypeId)
            jsonBody.put("otId", otNameId)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progressBar.value = 0
        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()
        apiService?.getOtSchedule(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(otTypeRetrofitCallback))
    }


}