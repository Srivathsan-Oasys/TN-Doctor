package com.oasys.digihealth.doctor.ui.out_patient.view_model

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
import com.oasys.digihealth.doctor.ui.out_patient.model.search_request_model.SearchPatientRequestModel
import com.oasys.digihealth.doctor.ui.out_patient.search_response_model.InPatientResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class InPatientViewModel(application: Application?) : AndroidViewModel(
    application!!
) {
    var errorText = MutableLiveData<String>()
    var progressBar = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var appPreferences: AppPreferences? = null

    init {
        progressBar.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)

    }


    fun searchAdmittedPatients(
        query: String,
        currentPage: Int,
        pageSize: Int,
        sortField: String,
        sortOrder: String,
        admittedPatientRetrofitCallback: RetrofitCallback<InPatientResponseModel>
    ) {

        val jsonBody = JSONObject()
        try {
            jsonBody.put("pageNo", 0)
            jsonBody.put("paginationSize", 10)
            jsonBody.put("sortField", "admission_date")
            jsonBody.put("sortOrder", "ASC")
            jsonBody.put("admission_status_uuid", 6)
            jsonBody.put(
                "admission_department_uuid",
                appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!
            )
            jsonBody.put("search", query)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        val searchPatientRequestModel = SearchPatientRequestModel()
        if (query.length > 10) {
            searchPatientRequestModel.mobile = ""
            searchPatientRequestModel.pin = query
        } else {
            searchPatientRequestModel.mobile = query
            searchPatientRequestModel.pin = ""
        }
        searchPatientRequestModel.pageNo = currentPage
        searchPatientRequestModel.paginationSize = pageSize
        searchPatientRequestModel.sortField = sortField
        searchPatientRequestModel.sortOrder = sortOrder
        searchPatientRequestModel.admission_status_uuid = 3.toString()
//        searchPatientRequestModel.admission_department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!


        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getAdmittedPAtientList(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, appPreferences?.getInt(AppConstants.FACILITY_UUID)!!, body
        )!!.enqueue(
            RetrofitMainCallback(admittedPatientRetrofitCallback)
        )

    }

    fun getPatientListNextPage(
        query: String,
        currentPage: Int,
        patientSearchNextRetrofitCallBack: RetrofitCallback<InPatientResponseModel>
    ) {
        val jsonBody = JSONObject()
        try {
            jsonBody.put("pageNo", 0)
            jsonBody.put("paginationSize", 10)
            jsonBody.put("sortField", "admission_date")
            jsonBody.put("sortOrder", "ASC")
            jsonBody.put("admission_status_uuid", 6)
            jsonBody.put(
                "admission_department_uuid",
                appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!
            )
            jsonBody.put("search", query)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        val searchPatientRequestModel = SearchPatientRequestModel()
        if (query.length > 10) {
            searchPatientRequestModel.mobile = ""
            searchPatientRequestModel.pin = query
        } else {
            searchPatientRequestModel.mobile = query
            searchPatientRequestModel.pin = ""
        }
        searchPatientRequestModel.pageNo = currentPage
        searchPatientRequestModel.paginationSize = currentPage
        searchPatientRequestModel.sortField = "modified_date"
        searchPatientRequestModel.sortOrder = "DESC"
        searchPatientRequestModel.admission_status_uuid = 3.toString()
//        searchPatientRequestModel.admission_department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!


        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getAdmittedPAtientList(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, appPreferences?.getInt(AppConstants.FACILITY_UUID)!!, body
        )!!.enqueue(
            RetrofitMainCallback(patientSearchNextRetrofitCallBack)
        )
    }

}