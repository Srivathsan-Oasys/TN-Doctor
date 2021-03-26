package com.hmis_tn.doctor.ui.out_patient.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppConstants.FACILITY_UUID
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitMainCallback
import com.hmis_tn.doctor.ui.out_patient.model.search_request_model.SearchPatientRequestModel
import com.hmis_tn.doctor.ui.out_patient.search_response_model.MyPatientsResponseModel
import com.hmis_tn.doctor.ui.out_patient.search_response_model.OldPatientResponseModule
import com.hmis_tn.doctor.ui.out_patient.search_response_model.SearchResponseModel
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject


class OutPatientViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {
    var errorText = MutableLiveData<String>()
    var progressBar = MutableLiveData<Int>()
    var progressBars = MutableLiveData<Int>()

    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var appPreferences: AppPreferences? = null

    init {
        progressBar.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()


    }


    fun searchPatient(
        facilityid: String?,
        departmentId: String,
        query: String,
        date: String,
        currentPage: Int,
        pageSize: Int,
        sortField: String,
        sortOrder: String,
        patientSearchRetrofitCallBack: RetrofitCallback<SearchResponseModel>
    ) {
        val searchPatientRequestModel = SearchPatientRequestModel()
        if (query.isNotEmpty()) {
            if (query.length > 10) {
                searchPatientRequestModel.mobile = ""
                searchPatientRequestModel.pin = query
            } else {
                searchPatientRequestModel.mobile = query
                searchPatientRequestModel.pin = ""
            }
        } else {
            searchPatientRequestModel.department_uuid = departmentId
            searchPatientRequestModel.facility_uuid = facilityid
            searchPatientRequestModel.registeredDate = date
        }
        searchPatientRequestModel.pageNo = currentPage
        searchPatientRequestModel.paginationSize = pageSize
        searchPatientRequestModel.sortField = sortField
        searchPatientRequestModel.sortOrder = sortOrder

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.searchOutPatient(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            appPreferences?.getInt(FACILITY_UUID)!!,
            searchPatientRequestModel
        )!!.enqueue(
            RetrofitMainCallback(patientSearchRetrofitCallBack)
        )
    }


    fun getPatientListNextPage(
        facilityid: String?,
        departmentId: String,
        query: String,
        date: String,
        currentPage: Int,
        patientSearchNextRetrofitCallBack: RetrofitCallback<SearchResponseModel>
    ) {

        val searchPatientRequestModel = SearchPatientRequestModel()
        if (query.isNotEmpty()) {
            if (query.length > 10) {
                searchPatientRequestModel.mobile = ""
                searchPatientRequestModel.pin = query
            } else {
                searchPatientRequestModel.mobile = query
                searchPatientRequestModel.pin = ""
            }
        } else {
            searchPatientRequestModel.registeredDate = date
            searchPatientRequestModel.facility_uuid = facilityid
            searchPatientRequestModel.department_uuid = departmentId
        }
        searchPatientRequestModel.pageNo = currentPage
        searchPatientRequestModel.paginationSize = 10
        searchPatientRequestModel.sortField = "modified_date"
        searchPatientRequestModel.sortOrder = "DESC"

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.searchOutPatient(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            appPreferences?.getInt(FACILITY_UUID)!!,
            searchPatientRequestModel
        )!!.enqueue(
            RetrofitMainCallback(patientSearchNextRetrofitCallBack)
        )
    }


    fun searchMyPatient(
        fromDate: String, toDate: String, departmentId: String,
        query: String,
        currentPage: Int,
        pageSize: Int,
        sortField: String,
        sortOrder: String,
        patientSearchRetrofitCallBack: RetrofitCallback<MyPatientsResponseModel>
    ) {

        val userDataStoreBeans = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("pageNo", currentPage)
            jsonBody.put("paginationSize", pageSize)
            jsonBody.put("sortField", sortField)
            jsonBody.put("sortOrder", sortOrder)
            jsonBody.put("from_date", fromDate)
            jsonBody.put("to_date", toDate)
            jsonBody.put("doctor_id", userDataStoreBeans?.uuid.toString())
            jsonBody.put("departmentId", departmentId)
            if (query.isNotEmpty()) {
                jsonBody.put("pd_mobile", query)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBars.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.searchMypatient(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            appPreferences?.getInt(FACILITY_UUID)!!,
            body
        )!!.enqueue(
            RetrofitMainCallback(patientSearchRetrofitCallBack)
        )
    }

    fun getMyPatientListNextPage(
        fromDate: String,
        toDate: String,
        departmentId: String,
        query: String,
        currentPage: Int,
        patientSearchNextRetrofitCallBack: RetrofitCallback<MyPatientsResponseModel>
    ) {

        val userDataStoreBeans = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("pageNo", currentPage)
            jsonBody.put("paginationSize", 10)
            jsonBody.put("sortField", "modified_date")
            jsonBody.put("sortOrder", "DESC")
            jsonBody.put("from_date", fromDate)
            jsonBody.put("to_date", toDate)
            jsonBody.put("doctor_id", userDataStoreBeans?.uuid.toString())
            jsonBody.put("departmentId", departmentId)
            if (query.isNotEmpty()) {
                jsonBody.put("pd_mobile", query)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBars.value = 0
        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.searchMypatient(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            appPreferences?.getInt(FACILITY_UUID)!!,
            body
        )!!.enqueue(
            RetrofitMainCallback(patientSearchNextRetrofitCallBack)
        )
    }

    fun getOldPatient(
        pinumber: String?,
        facilityid: Int?,
        oldPatientSearchRetrofitCallback: RetrofitCallback<OldPatientResponseModule>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("oldPin", pinumber)

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
        apiService?.oldPatient(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(oldPatientSearchRetrofitCallback))
    }


}