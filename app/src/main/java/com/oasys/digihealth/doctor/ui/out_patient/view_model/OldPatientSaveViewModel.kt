package com.oasys.digihealth.doctor.ui.out_patient.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.application.HmisApplication
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.ui.dashboard.model.CovidGenderResponseModel
import com.oasys.digihealth.doctor.ui.dashboard.model.CovidPeriodResponseModel
import com.oasys.digihealth.doctor.ui.dashboard.model.CovidSalutationTitleResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseModel
import com.oasys.digihealth.doctor.ui.out_patient.model.requestaddpatient.RequestAddPatient
import com.oasys.digihealth.doctor.ui.out_patient.model.responseaddpatient.ResponseAddPatient
import com.oasys.digihealth.doctor.ui.quick_reg.model.FacilityLocationResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject


class OldPatientSaveViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {
    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var progressBars = MutableLiveData<Int>()
    var facility_id: Int? = 0
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var appPreferences: AppPreferences? = null

    init {
        progress.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)

        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)


        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)

    }

    fun getCovidGenderList(
        facilityId: Int,
        covidGenderResponseCallback: RetrofitCallback<CovidGenderResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getCovidGender(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId
        ).enqueue(RetrofitMainCallback(covidGenderResponseCallback))
        return
    }

    fun getCovidPeriodList(
        facilityId: Int,
        covidPeriodResponseCallback: RetrofitCallback<CovidPeriodResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getCovidPeriod(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId
        ).enqueue(RetrofitMainCallback(covidPeriodResponseCallback))
        return
    }

    fun getCovidNameTitleList(
        facilityId: Int,
        covidSalutationResponseCallback: RetrofitCallback<CovidSalutationTitleResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getCovidNameTitle(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId
        ).enqueue(RetrofitMainCallback(covidSalutationResponseCallback))
        return
    }

    fun getAllDepartment(
        facilityID: Int?,
        favAddAllDepartmentCallBack: RetrofitCallback<FavAddAllDepatResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val jsonBody = JSONObject()

        try {
            jsonBody.put("facilityBased", true)
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

        apiService?.getFavddAllADepartmentList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityID!!,
            body
        ).enqueue(RetrofitMainCallback(favAddAllDepartmentCallBack))
        return

    }

    fun getFaciltyLocation(stateRetrofitCallback: RetrofitCallback<FacilityLocationResponseModel>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        val jsonBody = JSONObject()
        try {
            jsonBody.put("facility_uuid", facility_id)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getFacilityLocation(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, body
        ).enqueue(RetrofitMainCallback(stateRetrofitCallback))

        return
    }


    fun quickRegistrationSaveList(
        requestAddPatient: RequestAddPatient,
        responsePatienAdd: RetrofitCallback<ResponseAddPatient>
    ) {
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.addPatientsave(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            requestAddPatient
        ).enqueue(RetrofitMainCallback(responsePatienAdd))

    }

//    fun getOldPatient(oldpinnumber : String?,facilityid: Int?,  oldPatientSearchRetrofitCallback: RetrofitCallback<OLdPatientResponseModel>) {
//        if (!Utils.isNetworkConnected(getApplication())) {
//            errorText.value = getApplication<Application>().getString(R.string.no_internet)
//            return
//        }
//
//        val jsonBody = JSONObject()
//        try {
//            jsonBody.put("oldPin", oldpinnumber)
//
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//
//        val body = RequestBody.create(
//            okhttp3.MediaType.parse("application/json; charset=utf-8"),
//            jsonBody.toString()
//        )
//        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
//        progressBar.value = 0
//        val hmisApplication = HmisApplication.get(getApplication())
//        val apiService = hmisApplication.getRetrofitService()
//        apiService?.oldPatient(
//            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
//            userDataStoreBean?.uuid!!, facilityid!!,body)?.enqueue(RetrofitMainCallback(oldPatientSearchRetrofitCallback))
//    }


}