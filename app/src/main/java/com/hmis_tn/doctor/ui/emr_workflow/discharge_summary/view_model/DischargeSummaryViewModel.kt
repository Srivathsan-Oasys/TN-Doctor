package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitMainCallback
import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.*
import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model.DischargeSummaryListResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.previous_model.DischargeSummaryPreviousResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.nurse_desk.revertresponse.RevertRequest
import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.nurse_desk.revertresponse.RevertResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.save_model.SaveRequestModel
import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.save_model.SaveResponseModel
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject

class DischargeSummaryViewModel(
    application: Application
) : AndroidViewModel(
    application
) {
    var errorTextVisibility = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var appPreferences: AppPreferences? = null
    var errorText = MutableLiveData<String>()
    private var department_UUID: Int? = 0
    var facility_id: Int? = 0

    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        progress.value = 8
    }

    fun getDischargeSummaryDashBoardList(
        facility_id: Int,
        patientUuid: Int,
        encounterUuid: Int,
        dischargeAPICallBack: RetrofitCallback<DischargeSummaryListResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val apiService = HmisApplication.get(getApplication()).getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        Log.e("facility_id", "___" + facility_id)
        Log.e("encounterUuid", "___" + encounterUuid)
        Log.e("patientUuid", "___" + patientUuid)
        apiService?.getDischargeSummaryList(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id, patientUuid, encounterUuid

        )?.enqueue(RetrofitMainCallback(dischargeAPICallBack))
    }

    fun getDischargeSummaryTypeList(
        dischargeTypeAPICallBack: RetrofitCallback<ResDischargeType>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val apiService = HmisApplication.get(getApplication()).getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getDischargeSummaryType(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!
        )?.enqueue(RetrofitMainCallback(dischargeTypeAPICallBack))
    }

    fun getDischargeSummaryDeathType(dischargeDeathTypeAPICallBack: RetrofitCallback<ResDischargeDeathType>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val apiService = HmisApplication.get(getApplication()).getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getDischargeSummaryDeathType(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!
        )?.enqueue(RetrofitMainCallback(dischargeDeathTypeAPICallBack))
    }

    fun getDischargeSummaryPreviousData(
        patientUuid: Int,
        encounterUUID: Int,
        dischargePrevDataAPICallBack: RetrofitCallback<DischargeSummaryPreviousResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val apiService = HmisApplication.get(getApplication()).getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getDischargeSummaryPreviousData(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, patientUuid!!, encounterUUID
        )?.enqueue(RetrofitMainCallback(dischargePrevDataAPICallBack))
    }

    fun getDischargeSummaryDefaultTemplate(
        dischargeTemplateAPICallBack: RetrofitCallback<ResDischargeSummaryDefaultTemplate>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val apiService = HmisApplication.get(getApplication()).getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getDischargeSummaryDefaultTemplate(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!
        )?.enqueue(RetrofitMainCallback(dischargeTemplateAPICallBack))
    }

    fun getDischargeNoteTemplate(
        addDocumentTypeResponseCallback: RetrofitCallback<ResDischargeSummaryTemplate>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getDischargeNoteTemplate(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!
        )?.enqueue(RetrofitMainCallback(addDocumentTypeResponseCallback))
        return
    }

    fun setDefaultTemplate(
        reqDefaultTemplate: ReqDefaultTemplate,
        callbackSetDefaultTemplate: RetrofitCallback<ResponseBody>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.setDischargeDefaultTemplate(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, reqDefaultTemplate
        )?.enqueue(RetrofitMainCallback(callbackSetDefaultTemplate))
    }

    fun getEncounter(
        facility_id: Int?,
        patientUuid: Int,
        doctorId: Int,
        encounterType: Int, departmentID: Int,
        fetchEncounterRetrofitCallBack: RetrofitCallback<EncounterResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            department_UUID
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getDischargeEncounters(
            acceptLanguage = AppConstants.ACCEPT_LANGUAGE_EN,
            authorization = AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            user_uuid = userDataStoreBean?.uuid!!,
            facility_uuid = facility_id!!,
            patientId = patientUuid,
            doctorId = doctorId,
            departmentId = departmentID,
            encounterType = encounterType
        )!!.enqueue(
            RetrofitMainCallback(fetchEncounterRetrofitCallBack)
        )
    }

    fun revertData(
        facilityId: Int,
        revert: RevertRequest,
        callbackRevert: RetrofitCallback<RevertResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val apiService = HmisApplication.get(getApplication()).getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()


        apiService?.revertDischargePatient(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityId, revert

        )?.enqueue(RetrofitMainCallback(callbackRevert))


    }

    fun GetDischargePDFf(
        requestPDF: DischargePDFRequestModel,
        GetPDFRetrofitCallback: RetrofitCallback<ResponseBody>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getDischargePDF(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, 427
        )?.enqueue(RetrofitMainCallback(GetPDFRetrofitCallback))

    }

    fun dischargeSave(
        facility_id: Int?,
        saveRequest: SaveRequestModel,
        configFinalRetrofitCallBack: RetrofitCallback<SaveResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.getDischargeSave(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            saveRequest
        )?.enqueue(RetrofitMainCallback(configFinalRetrofitCallBack))
        return

    }

    fun dischargeApprove(
        facility_id: Int?,
        saveRequest: SaveRequestModel,
        configFinalRetrofitCallBack: RetrofitCallback<SaveResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        // HERE UPDATE THE API FOR APPROVE
        apiService?.getDischargeSApproval(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            saveRequest
        )?.enqueue(RetrofitMainCallback(configFinalRetrofitCallBack))
        return

    }

    fun getDoctorName(doctornameResponseCallback: RetrofitCallback<DischargeSummaryDoctorNameResponseModel>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("user_type_code", "Physician")
            jsonBody.put("is_anaesthetist", 0)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        //  progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getDischargeSummaryDoctorName(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, AppConstants?.ACCEPT_LANGUAGE_EN,
            body
        )?.enqueue(RetrofitMainCallback(doctornameResponseCallback))
    }
}