package com.hmis_tn.doctor.ui.institution.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitMainCallback
import com.hmis_tn.doctor.ui.institution.common_departmant.model.DepartmentResponseModel
import com.hmis_tn.doctor.ui.institution.lmis.model.LocationMasterResponseModel
import com.hmis_tn.doctor.ui.login.model.institution_response.InstitutionResponseModel
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class InstituteViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {

    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    init {
        progress.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
    }

    fun getInstitutionList(
        selectedItemID: Int?,
        instituteRetrofitCallBack: RetrofitCallback<com.hmis_tn.doctor.ui.institute.model.InstitutionResponseModel>
    ) {


        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("health_office_uuid", selectedItemID)
            jsonBody.put("Id", userDataStoreBean?.uuid!!)
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

        apiService?.getInstitutionList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            body
        )?.enqueue(RetrofitMainCallback(instituteRetrofitCallBack))
        return
    }

    fun getDepartmentList(
        facilitylevelID: Int?,
        departmentRetrofitCallBack: RetrofitCallback<DepartmentResponseModel>
    ) {


        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("facility_uuid", facilitylevelID)
            jsonBody.put("Id", userDataStoreBean?.uuid!!)
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

        apiService?.getDepartmentList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            body
        )?.enqueue(RetrofitMainCallback(departmentRetrofitCallBack))
        return
    }


    fun getfacilityCallback(facilityCallback: RetrofitCallback<InstitutionResponseModel>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        val jsonBody = JSONObject()
        try {
            jsonBody.put("userId", userDataStoreBean?.uuid!!.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        progress.value = 0
        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()
        apiService?.getFaciltyList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, body
        )?.enqueue(RetrofitMainCallback(facilityCallback))
    }


    fun getLocationMaster(
        department: ArrayList<Int>,
        facility: Int,
        stateRetrofitCallback: RetrofitCallback<LocationMasterResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("facility_uuid", facility)
            jsonBody.put("paginationSize", 1000)
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

        apiService?.getLocationMasterLogin(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility,
            body
        )?.enqueue(RetrofitMainCallback(stateRetrofitCallback))

        return
    }

    fun getLocationMaster(
        facility: Int,
        stateRetrofitCallback: RetrofitCallback<LocationMasterResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("facility_uuid", facility)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

//        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getRmisLocationMaster(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility,
            body
        )?.enqueue(RetrofitMainCallback(stateRetrofitCallback))

        return
    }
}