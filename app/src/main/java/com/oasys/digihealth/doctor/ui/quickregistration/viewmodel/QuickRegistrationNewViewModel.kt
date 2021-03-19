package com.oasys.digihealth.doctor.ui.quickregistration.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.application.HmisApplication
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.ui.dashboard.model.*
import com.oasys.digihealth.doctor.ui.dashboard.model.registration.*
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.CreateEncounterRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.Encounter
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.EncounterDoctor
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.opcorrection.model.OpCorrectionRequest
import com.oasys.digihealth.doctor.ui.quick_reg.model.*
import com.oasys.digihealth.doctor.ui.quick_reg.model.activitysession.ResponseActivitySession
import com.oasys.digihealth.doctor.ui.quick_reg.model.request.LabNameSearchResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.request.QuickRegistrationRequestModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.request.QuickRegistrationUpdateRequestModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.request.SaveOrderRequestModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.session.ResponseSesionModule
import com.oasys.digihealth.doctor.ui.quickregistration.model.*
import com.oasys.digihealth.doctor.ui.quickregistration.model.response.QuickelementRoleResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class QuickRegistrationNewViewModel(
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

    var department_UUID: Int? = 0


    var appPreferences: AppPreferences? = null


    init {

        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)

        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)

        progress.value = 8

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)

        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
    }


    fun quickRegistrationSaveList(
        requestRegistration: QuickRegistrationRequestModel,
        PatientNameRetrofitCallback: RetrofitCallback<QuickRegistrationSaveResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.quickRegistrationSave(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            requestRegistration
        ).enqueue(RetrofitMainCallback(PatientNameRetrofitCallback))

    }

    fun quickRegistrationSaveList(
        requestRegistration: RegistrationUatSaveRequest,
        PatientNameRetrofitCallback: RetrofitCallback<QuickRegistrationSaveResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.quickRegistrationSave(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            requestRegistration
        ).enqueue(RetrofitMainCallback(PatientNameRetrofitCallback))

    }

    fun quickRegistrationUsingOldpinSaveList(
        requestRegistration: QuickRegWItholdpinSaveReq,
        PatientNameRetrofitCallback: RetrofitCallback<QuickRegistrationSaveResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.quickRegistrationSave(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            requestRegistration
        ).enqueue(RetrofitMainCallback(PatientNameRetrofitCallback))

    }

    fun getCovidNameTitleList(
        facilityId: Int,
        covidSalutationResponseCallback: RetrofitCallback<CovidSalutationTitleResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        // progress.value = 0
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
    //updateQuickRegistration

    fun quickRegistrationUpdate(
        requestRegistration: QuickRegistrationUpdateRequestModel,
        updateQuickRegistrationRetrofitCallback: RetrofitCallback<QuickRegistrationUpdateResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        //   progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.updateQuickRegistration(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            requestRegistration
        ).enqueue(RetrofitMainCallback(updateQuickRegistrationRetrofitCallback))

    }

    fun quickRegistrationUpdateformQuick(
        requestRegistration: QuickRegistrationUpdateRequest,
        updateQuickRegistrationRetrofitCallback: RetrofitCallback<QuickRegistrationUpdateResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.updateQuickRegistrationFromQuick(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            requestRegistration
        ).enqueue(RetrofitMainCallback(updateQuickRegistrationRetrofitCallback))

    }

    fun quickRegistrationUpdateformOPcorrection(
        requestRegistration: OpCorrectionRequest,
        updateQuickRegistrationRetrofitCallback: RetrofitCallback<QuickRegistrationUpdateResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.updateQuickRegistrationFromOpCorrection(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            requestRegistration
        ).enqueue(RetrofitMainCallback(updateQuickRegistrationRetrofitCallback))

    }

    fun quickRegistrationUpdateformQuick(
        requestRegistration: QuickRegistrationUpdateRequestWitholdPin,
        updateQuickRegistrationRetrofitCallback: RetrofitCallback<QuickRegistrationUpdateResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.updateQuickRegistrationFromQuick(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            requestRegistration
        ).enqueue(RetrofitMainCallback(updateQuickRegistrationRetrofitCallback))

    }

    fun GetPDFf(
        requestPDF: PDFRequestModel,
        GetPDFRetrofitCallback: RetrofitCallback<ResponseBody>
    ) {
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getPDF(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            requestPDF
        ).enqueue(RetrofitMainCallback(GetPDFRetrofitCallback))

    }


    fun getCovidPeriodList(
        facilityId: Int,
        covidPeriodResponseCallback: RetrofitCallback<CovidPeriodResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        //  progress.value = 0
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


    fun getAll(
        teblename: String,
        covidPeriodResponseCallback: RetrofitCallback<GetAllResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        //   progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()


        val jsonBody = JSONObject()

        try {
            jsonBody.put("table_name", teblename)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        apiService?.getAllDetails(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, body

        ).enqueue(RetrofitMainCallback(covidPeriodResponseCallback))
        return
    }

    fun getCommunity(
        tablename: String,
        specimenListRetrofitCallBack: RetrofitCallback<ResponseSpicemanType>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        //progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()


        apiService?.getSpecimen_Type(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, AppConstants.ACCEPT_LANGUAGE_EN,
            facility_id!!, tablename
        ).enqueue(RetrofitMainCallback(specimenListRetrofitCallBack))
        return
    }

    fun getCovidGenderList(
        facilityId: Int,
        covidGenderResponseCallback: RetrofitCallback<CovidGenderResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        //     progress.value = 0
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

    fun getCovidNationalityList(
        query: String,
        covidNationalityResponseCallback: RetrofitCallback<CovidNationalityResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        //     progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getAllActiveNationality(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!
        ).enqueue(RetrofitMainCallback(covidNationalityResponseCallback))
        return
    }

    fun getStateList(
        country_id: Int,
        stateRetrofitCallback: RetrofitCallback<StateListResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("country_uuid", country_id)
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

        apiService?.getState(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        ).enqueue(RetrofitMainCallback(stateRetrofitCallback))


        return
    }

    fun getDistrict(
        uuid: Int,
        distictRetrofitCallback: RetrofitCallback<DistrictListResponseModel>
    ) {


        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("Id", uuid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        //      progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getDistict(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        ).enqueue(RetrofitMainCallback(distictRetrofitCallback))

        return

    }


    fun getBlock(uuid: Int, distictRetrofitCallback: RetrofitCallback<BlockZoneResponseModel>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("district_uuid", uuid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        //     progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getBlockZone(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        ).enqueue(RetrofitMainCallback(distictRetrofitCallback))

        return

    }

    fun getPrevilage(
        facilityId: Int,
        roleID: Int?,
        getPrivilageRetrofitCallback: RetrofitCallback<ResponsePrivillageModule>
    ) {

        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val jsonBody = JSONObject()
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        try {
            jsonBody.put("activityCode", AppConstants.QUICKREGISTERCODE)
            jsonBody.put("roleId", roleID)

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
        apiService?.getPrivilliageModule(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, "en",
            userDataStoreBean?.uuid!!, facilityId!!,
            body
        ).enqueue(RetrofitMainCallback(getPrivilageRetrofitCallback))

    }

    fun getTextMethod(
        facilityId: Int,
        ResponseTestMethodRetrofitCallback: RetrofitCallback<ResponseTestMethod>
    ) {

        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {
            jsonBody.put("pageNo", 0)
            jsonBody.put("paginationSize", 1000)
            jsonBody.put("status", 1)
            jsonBody.put("table_name", "type_of_method")
            jsonBody.put("sortOrder", "ASC")
            jsonBody.put("sortField", "display_order")
            jsonBody.put("search", "")


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

        apiService?.getTestMethod(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityId!!,
            body
        ).enqueue(RetrofitMainCallback(ResponseTestMethodRetrofitCallback))

    }

    fun getTextMethod1(
        facilityId: Int,
        ResponseTestMethodRetrofitCallback: RetrofitCallback<ResponseTestMethod>
    ) {

        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {
            jsonBody.put("pageNo", 0)
            jsonBody.put("paginationSize", 1000)
            jsonBody.put("status", 1)
            jsonBody.put("table_name", "sample_type")
            jsonBody.put("sortOrder", "ASC")
            jsonBody.put("sortField", "display_order")
            jsonBody.put("search", "")


        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        //   progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getTestMethod(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityId!!,
            body
        ).enqueue(RetrofitMainCallback(ResponseTestMethodRetrofitCallback))

    }

    fun getLabName(
        search: String,
        GetPDFRetrofitCallback: RetrofitCallback<LabNameSearchResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        val jsonBody = JSONObject()
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }


        try {
            jsonBody.put("searchKey", "search")
            jsonBody.put("search", search)
            jsonBody.put("pageNo", 0)
            jsonBody.put("is_lab_center", true)
            jsonBody.put("paginationSize", 10)


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

        apiService?.getLabname(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, body
        ).enqueue(RetrofitMainCallback(GetPDFRetrofitCallback))

    }

    fun getLocationMaster(
        facility: Int,
        stateRetrofitCallback: RetrofitCallback<LocationMasterResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
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

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getLocationMaster(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        ).enqueue(RetrofitMainCallback(stateRetrofitCallback))

        return
    }

    fun getLabNameInList(
        id: Int,
        stateRetrofitCallback: RetrofitCallback<GetLabNameListResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("Id", id)
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

        apiService?.getLabNameinList(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, body
        ).enqueue(RetrofitMainCallback(stateRetrofitCallback))

        return
    }


    fun getApplicationRules(stateRetrofitCallback: RetrofitCallback<GetApplicationRulesResponseModel>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()


        //      progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getApplicationRules(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!
        ).enqueue(RetrofitMainCallback(stateRetrofitCallback))

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

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getFacilityLocation(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, body
        ).enqueue(RetrofitMainCallback(stateRetrofitCallback))

        return
    }

    fun getAllDepartment(
        req: GetAllDepartmentRequest?,
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

        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getSearchDepartment(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            req
        ).enqueue(RetrofitMainCallback(favAddAllDepartmentCallBack))
        return

    }


    fun getAllSchema(
        req: SchemaRequest?,
        favAddAllDepartmentCallBack: RetrofitCallback<SchemaResponseModel>
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

        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getAllSchema(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            req
        ).enqueue(RetrofitMainCallback(favAddAllDepartmentCallBack))
        return

    }

    fun getSchemaName(
        uuid: Int,
        favAddAllDepartmentCallBack: RetrofitCallback<SchemaNameResponce>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val jsonBody = JSONObject()

        try {
            jsonBody.put("Id", uuid)

            jsonBody.put("uuid", uuid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getSchemaName(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        ).enqueue(RetrofitMainCallback(favAddAllDepartmentCallBack))
        return

    }

    fun getVillage(uuid: Int, talukRetrofitCallback: RetrofitCallback<VilliageListResponceModel>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("talukId", uuid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        //    progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getVillage(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        ).enqueue(RetrofitMainCallback(talukRetrofitCallback))

    }

    fun getTaluk(uuid: Int, talukRetrofitCallback: RetrofitCallback<TalukListResponseModel>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("Id", uuid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        //      progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getTaluk(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        ).enqueue(RetrofitMainCallback(talukRetrofitCallback))

    }


    fun getDepartmentList(
        FavdepartmentRetrofitCallBack: RetrofitCallback<FavAddResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("uuid", department_UUID)
            jsonBody.put("registrationBased", true)
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

        apiService?.getFavDepartmentList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        ).enqueue(RetrofitMainCallback(FavdepartmentRetrofitCallBack))
        return
    }

    fun getLocationAPI(stateRetrofitCallback: RetrofitCallback<LocationMasterResponseModelX>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        val jsonBody = JSONObject()
        try {

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

        apiService?.getLocation(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, body
        ).enqueue(RetrofitMainCallback(stateRetrofitCallback))

        return
    }

    fun getTotest(complaintSearchRetrofitCallBack: RetrofitCallback<GettestResponseModel>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("search", "COVID")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        //      progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        apiService?.getTest(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!,
            body
        ).enqueue(RetrofitMainCallback(complaintSearchRetrofitCallBack))
        return
    }

    fun getReference(
        labTypeRetrofitCallback: RetrofitCallback<GetReferenceResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()

        try {
            jsonBody.put("table_name", "order_priority")
            jsonBody.put("sortField", "uuid")
            jsonBody.put("sortOrder", "DESC")
            jsonBody.put("status", 1)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        //    progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getReference(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!,
            body
        ).enqueue(RetrofitMainCallback(labTypeRetrofitCallback))
        return

    }


    fun searchPatient(
        query: String,
        pin: String,
        mobile: String,
        pagesize: Int,
        curretpage: Int,
        aather: String,
        patientSearchRetrofitCallBack: RetrofitCallback<QuickSearchResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val searchPatientRequestModelCovid = SearchPatientRequestModelCovid()

        searchPatientRequestModelCovid.mobile = mobile
        searchPatientRequestModelCovid.pin = pin
        searchPatientRequestModelCovid.searchKeyWord = query
        searchPatientRequestModelCovid.pageNo = curretpage
        searchPatientRequestModelCovid.paginationSize = pagesize
        searchPatientRequestModelCovid.sortField = "patient_visits[0].registered_date"
        searchPatientRequestModelCovid.sortOrder = "DESC"
        searchPatientRequestModelCovid.aadhaar = aather

        val req = Gson().toJson(searchPatientRequestModelCovid)
        Log.e("data", req.toString())

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        apiService?.searchOutPatientcovid(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean.access_token,
            userDataStoreBean.uuid!!,
            appPreferences?.getInt(AppConstants.FACILITY_UUID)!!,
            searchPatientRequestModelCovid
        )!!.enqueue(
            RetrofitMainCallback(patientSearchRetrofitCallBack)
        )
    }


    fun searchPatient(
        searchPatientRequestModelCovid: SearchPatientRequestModelCovid,

        patientSearchRetrofitCallBack: RetrofitCallback<QuickSearchResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }


        val req = Gson().toJson(searchPatientRequestModelCovid)
        Log.e("data", req.toString())

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        apiService?.searchOutPatientcovid(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean.access_token,
            userDataStoreBean.uuid!!,
            appPreferences?.getInt(AppConstants.FACILITY_UUID)!!,
            searchPatientRequestModelCovid
        )!!.enqueue(
            RetrofitMainCallback(patientSearchRetrofitCallBack)
        )
    }

    fun searchOldPin(
        pin: String,
        patientSearchRetrofitCallBack: RetrofitCallback<QuickSearchResponseModel>
    ) {
        val searchPatientRequestModelCovid = SearchPatientRequestModelCovid()

        searchPatientRequestModelCovid.oldPin = pin

        val req = Gson().toJson(searchPatientRequestModelCovid)
        Log.e("data", req.toString())

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        apiService?.searchOldPincovid(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean.access_token,
            userDataStoreBean.uuid!!,
            appPreferences?.getInt(AppConstants.FACILITY_UUID)!!,
            searchPatientRequestModelCovid
        )!!.enqueue(
            RetrofitMainCallback(patientSearchRetrofitCallBack)
        )
    }


    fun getSearchNameCovidPatient(PatientNameRetrofitCallback: RetrofitCallback<CovidRegistrationSearchResponseModel>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("searchKeyWord", "")


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

        apiService?.getCovidPatientSearch(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, AppConstants?.ACCEPT_LANGUAGE_EN, facility_id!!,
            body
        ).enqueue(RetrofitMainCallback(PatientNameRetrofitCallback))

    }

    @SuppressLint("SimpleDateFormat")
    fun createEncounter(
        createEncounterCallback: RetrofitCallback<CreateEncounterResponseModel>, patientUUId: Int
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")

        val dateInString = sdf.format(Date())

        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        val createEncounterRequestModel = CreateEncounterRequestModel()

        val encounter = Encounter()
        encounter.admission_request_uuid = 0
        encounter.admission_uuid = 0
        encounter.appointment_uuid = 0
        encounter.department_uuid = appPreferences?.getInt(
            AppConstants.DEPARTMENT_UUID
        )
        encounter.discharge_type_uuid = 0
        encounter.encounter_identifier = 0
        encounter.encounter_priority_uuid = 0
        encounter.encounter_status_uuid = 0
        encounter.encounter_type_uuid = 1
        encounter.facility_uuid = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        encounter.patient_uuid = patientUUId

        createEncounterRequestModel.encounter = encounter

        val encounterDoctor = EncounterDoctor()
        encounterDoctor.department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        encounterDoctor.dept_visit_type_uuid = 0
        encounterDoctor.doctor_uuid = userDataStoreBean.uuid
        encounterDoctor.doctor_visit_type_uuid = 0
        encounterDoctor.patient_uuid = patientUUId
        encounterDoctor.session_type_uuid = 0
        encounterDoctor.speciality_uuid = 0
        encounterDoctor.sub_deparment_uuid = 0
        encounterDoctor.visit_type_uuid = 1
        encounterDoctor.tat_start_time = dateInString
        encounterDoctor.tat_end_time = dateInString

        createEncounterRequestModel.encounterDoctor = encounterDoctor

        apiService?.createEncounter(
            AppConstants.BEARER_AUTH + userDataStoreBean.access_token,
            userDataStoreBean.uuid!!,
            createEncounterRequestModel

        )!!.enqueue(
            RetrofitMainCallback(createEncounterCallback)
        )
    }

    fun getSaveOrder(
        request: SaveOrderRequestModel,
        PatientNameRetrofitCallback: RetrofitCallback<SaveOrderResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.saveOrder(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            request
        ).enqueue(RetrofitMainCallback(PatientNameRetrofitCallback))

    }

    fun getSession(
        facilityId: Int,
        sessionResponseCallback: RetrofitCallback<ResponseSesionModule>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("facilityId", facilityId)
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

        apiService?.getSessionTimer(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, AppConstants?.ACCEPT_LANGUAGE_EN,
            body
        ).enqueue(RetrofitMainCallback(sessionResponseCallback))
    }

    fun getActivitySession(
        facilityId: Int,
        activitysessionResponseCallback: RetrofitCallback<ResponseActivitySession>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        //     progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getActivitySession(
            "en",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId
        ).enqueue(RetrofitMainCallback(activitysessionResponseCallback))
        return

    }

    fun getActivityRole(
        facilityId: Int,
        activityPrivilageRetrofitCallback: RetrofitCallback<QuickelementRoleResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        //     progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getRoleControlQuick(
            "en",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId, 2
        ).enqueue(RetrofitMainCallback(activityPrivilageRetrofitCallback))
        return


    }


}