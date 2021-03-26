package com.hmis_tn.doctor.ui.institute.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitMainCallback
import com.hmis_tn.doctor.ui.emr_workflow.history.surgery.model.response.SurgeryInstitutionResponseModel
import com.hmis_tn.doctor.ui.institute.model.GetLocationRequest
import com.hmis_tn.doctor.ui.institute.model.InstitutionResponseModel
import com.hmis_tn.doctor.ui.institute.model.Phermisiun.StoreListResponseModel
import com.hmis_tn.doctor.ui.institute.model.wardList.WardListResponseModel
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class InstituteViewModel(
    application: Application?,
    private var officeRetrofitCallBack: RetrofitCallback<com.hmis_tn.doctor.ui.login.model.office_response.OfficeResponseModel>
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

    fun getOfficeList() {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("user_uuid", userDataStoreBean?.uuid!!)
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

        println("AppConstantsBEARER_AUTqewfregtry5= ${AppConstants.BEARER_AUTH + userDataStoreBean?.access_token}")

        apiService?.getOfficeList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            body
        )?.enqueue(RetrofitMainCallback(officeRetrofitCallBack))
        return
    }

    fun getInstitutionList(
        selectedItemID: Int?,
        instituteRetrofitCallBack: RetrofitCallback<InstitutionResponseModel>
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
        departmentRetrofitCallBack: RetrofitCallback<com.hmis_tn.doctor.ui.institution.common_departmant.model.DepartmentResponseModel>
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


    fun getWardList(
        facilitylevelID: Int?,
        departmentRetrofitCallBack: RetrofitCallback<WardListResponseModel>
    ) {


        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("facility_uuid", facilitylevelID)
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

        apiService?.getWardList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            body
        )?.enqueue(RetrofitMainCallback(departmentRetrofitCallBack))
        return
    }

    fun getfacilityCallback(facilityCallback: RetrofitCallback<SurgeryInstitutionResponseModel>) {
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
        apiService?.getFaciltyCheck(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, body
        )?.enqueue(RetrofitMainCallback(facilityCallback))
    }


    fun getLocationMaster(
        department: ArrayList<Int>,
        facility: Int,
        stateRetrofitCallback: RetrofitCallback<com.hmis_tn.doctor.ui.institution.lmis.model.LocationMasterResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("facility_uuid", facility)
            jsonBody.put("department_uuid", department)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        val request: GetLocationRequest = GetLocationRequest()

        request.facility_uuid = facility

        // request.department_uuid=department

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getLocationMasterLogin(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility,
            body
        )?.enqueue(RetrofitMainCallback(stateRetrofitCallback))

        return
    }

    fun getStoreList(
        facilitylevelID: Int?,
        departmentRetrofitCallBack: RetrofitCallback<StoreListResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()


        val jsonBody = JSONObject()
        try {
            jsonBody.put("facility_uuid", facilitylevelID)
            jsonBody.put("pageNo", 1)
            jsonBody.put("paginationSize", 0)
            jsonBody.put("search", "")
            jsonBody.put("sortField", "code")
            jsonBody.put("sortOrder", "DESC")
            jsonBody.put("user_uuid", userDataStoreBean!!.uuid)
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

        apiService?.getStoreList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            body
        )?.enqueue(RetrofitMainCallback(departmentRetrofitCallBack))
        return

    }

    fun getLocationMaster(
        facility: Int,
        stateRetrofitCallback: RetrofitCallback<com.hmis_tn.doctor.ui.institution.lmis.model.LocationMasterResponseModel>
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