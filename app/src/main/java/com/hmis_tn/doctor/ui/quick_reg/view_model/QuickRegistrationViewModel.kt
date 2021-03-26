package com.hmis_tn.doctor.ui.quick_reg.view_model

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitMainCallback
import com.hmis_tn.doctor.ui.dashboard.model.CovidGenderResponseModel
import com.hmis_tn.doctor.ui.dashboard.model.CovidNationalityResponseModel
import com.hmis_tn.doctor.ui.dashboard.model.CovidPeriodResponseModel
import com.hmis_tn.doctor.ui.dashboard.model.CovidSalutationTitleResponseModel
import com.hmis_tn.doctor.ui.dashboard.model.registration.CovidRegistrationSearchResponseModel
import com.hmis_tn.doctor.ui.dashboard.model.registration.DistrictListResponseModel
import com.hmis_tn.doctor.ui.dashboard.model.registration.StateListResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.CreateEncounterRequestModel
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.Encounter
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.EncounterDoctor
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.*
import com.hmis_tn.doctor.ui.quick_reg.model.request.LabNameSearchResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.request.QuickRegistrationRequestModel
import com.hmis_tn.doctor.ui.quick_reg.model.request.QuickRegistrationUpdateRequestModel
import com.hmis_tn.doctor.ui.quick_reg.model.request.SaveOrderRequestModel
import com.hmis_tn.doctor.ui.quickregistration.model.response.PDSResponseModule
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class QuickRegistrationViewModel(
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

    var departMentId: Int? = 0

    var appPreferences: AppPreferences? = null


    init {

        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)

        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)

        progress.value = 8
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)

        departMentId = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
    }


    fun quickRegistrationSaveList(
        requestRegistration: QuickRegistrationRequestModel,
        PatientNameRetrofitCallback: RetrofitCallback<QuickRegistrationSaveResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

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
        )?.enqueue(RetrofitMainCallback(PatientNameRetrofitCallback))
    }
    //updateQuickRegistration

    fun quickRegistrationUpdate(
        requestRegistration: QuickRegistrationUpdateRequestModel,
        updateQuickRegistrationRetrofitCallback: RetrofitCallback<QuickRegistrationUpdateResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.updateQuickRegistration(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            requestRegistration
        )?.enqueue(RetrofitMainCallback(updateQuickRegistrationRetrofitCallback))

    }

    fun GetPDFf(
        requestPDF: PDFRequestModel,
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

        apiService?.getPDF(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            requestPDF
        )?.enqueue(RetrofitMainCallback(GetPDFRetrofitCallback))

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
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getCovidPeriod(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId
        )?.enqueue(RetrofitMainCallback(covidPeriodResponseCallback))
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
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getCovidGender(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId
        )?.enqueue(RetrofitMainCallback(covidGenderResponseCallback))
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
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getNationality(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!,
            query
        )?.enqueue(RetrofitMainCallback(covidNationalityResponseCallback))
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
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
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

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getState(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(stateRetrofitCallback))


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
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
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

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getDistict(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(distictRetrofitCallback))

        return

    }


    fun getBlock(uuid: Int, distictRetrofitCallback: RetrofitCallback<BlockZoneResponseModel>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
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

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getBlockZone(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(distictRetrofitCallback))

        return

    }

    fun getPrevilage(
        facilityId: Int,
        roleID: Int?,
        getPrivilageRetrofitCallback: RetrofitCallback<ResponsePrivillageModule>
    ) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        try {
            jsonBody.put("activityCode", AppConstants.COVIDREGISTERCODE)
            jsonBody.put("roleId", roleID)

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
        apiService?.getPrivilliageModule(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, "en",
            userDataStoreBean?.uuid!!, facilityId,
            body
        )?.enqueue(RetrofitMainCallback(getPrivilageRetrofitCallback))

    }

    fun getTextMethod(
        facilityId: Int,
        ResponseTestMethodRetrofitCallback: RetrofitCallback<ResponseTestMethod>
    ) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
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

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getTestMethod(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityId,
            body
        )?.enqueue(RetrofitMainCallback(ResponseTestMethodRetrofitCallback))

    }

    fun getTextMethod1(
        facilityId: Int,
        ResponseTestMethodRetrofitCallback: RetrofitCallback<ResponseTestMethod>
    ) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
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

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getTestMethod(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityId,
            body
        )?.enqueue(RetrofitMainCallback(ResponseTestMethodRetrofitCallback))

    }

    fun getLabName(
        search: String,
        GetPDFRetrofitCallback: RetrofitCallback<LabNameSearchResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

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
        )?.enqueue(RetrofitMainCallback(GetPDFRetrofitCallback))

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

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getLocationMaster(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(stateRetrofitCallback))

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
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
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
        )?.enqueue(RetrofitMainCallback(stateRetrofitCallback))

        return
    }


    fun getApplicationRules(stateRetrofitCallback: RetrofitCallback<GetApplicationRulesResponseModel>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()



        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getApplicationRules(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!
        )?.enqueue(RetrofitMainCallback(stateRetrofitCallback))

        return
    }


    fun getFaciltyLocation(stateRetrofitCallback: RetrofitCallback<FacilityLocationResponseModel>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

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
        )?.enqueue(RetrofitMainCallback(stateRetrofitCallback))

        return
    }


    fun gettolocationmapid(
        facility_uuid: Int,
        testMasterID: Int,
        stateRetrofitCallback: RetrofitCallback<MapListResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        val jsonBody = JSONObject()
        try {


            jsonBody.put("test_master_uuid", testMasterID)

            jsonBody.put("profile_master_uuid", null)

            jsonBody.put("from_department_uuid", departMentId)

            // jsonBody.put("facility_uuid", facility_uuid)


/*
            test_master_uuid: 192
            profile_master_uuid: null
            from_department_uuid: "88"
*/

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

        apiService?.gettolocationmapid(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, body
        )?.enqueue(RetrofitMainCallback(stateRetrofitCallback))

        return
    }
/*
    fun getLocationAPI(stateRetrofitCallback: RetrofitCallback<LocationMasterResponseModelX>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        val jsonBody = JSONObject()
        try {

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

        apiService?.getLocation(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,facility_id!!,body
        )?.enqueue(RetrofitMainCallback(stateRetrofitCallback))

        return
    }*/

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
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getTest(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(complaintSearchRetrofitCallBack))
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
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getReference(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(labTypeRetrofitCallback))
        return

    }


    fun searchPatient(
        query: String,
        pin: String,
        mobile: String,

        patientSearchRetrofitCallBack: RetrofitCallback<QuickSearchResponseModel>
    ) {
        val searchPatientRequestModelCovid = SearchPatientRequestModelCovid()

        searchPatientRequestModelCovid.mobile = mobile
        searchPatientRequestModelCovid.pin = pin
        searchPatientRequestModelCovid.searchKeyWord = query


        val req = Gson().toJson(searchPatientRequestModelCovid)
        Log.e("data", req.toString())

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.searchOutPatientcovid(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
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
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
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
            userDataStoreBean?.uuid!!, AppConstants.ACCEPT_LANGUAGE_EN, facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(PatientNameRetrofitCallback))

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
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

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
        encounterDoctor.doctor_uuid = userDataStoreBean?.uuid
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
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
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
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.saveOrder(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            request
        )?.enqueue(RetrofitMainCallback(PatientNameRetrofitCallback))

    }

    fun searchPatient(
        query: String,
        pin: String,
        mobile: String,
        pagesize: Int,
        curretpage: Int,
        aatherNo: String,
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
        searchPatientRequestModelCovid.aadhaar = aatherNo

        val req = Gson().toJson(searchPatientRequestModelCovid)
        Log.e("data", req.toString())

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.searchOutPatientcovid(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            appPreferences?.getInt(AppConstants.FACILITY_UUID)!!,
            searchPatientRequestModelCovid
        )!!.enqueue(
            RetrofitMainCallback(patientSearchRetrofitCallBack)
        )
    }

    fun searchPatientSecond(
        query: String,
        pin: String,
        mobile: String,
        pagesize: Int,
        curretpage: Int,

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

        val req = Gson().toJson(searchPatientRequestModelCovid)
        Log.e("data", req.toString())

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.searchOutPatientcovid(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            appPreferences?.getInt(AppConstants.FACILITY_UUID)!!,
            searchPatientRequestModelCovid
        )!!.enqueue(
            RetrofitMainCallback(patientSearchRetrofitCallBack)
        )
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
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getCovidNameTitle(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId
        )?.enqueue(RetrofitMainCallback(covidSalutationResponseCallback))
        return
    }

    fun searchPDSPatient(
        pdsNumber: String,
        patientSearchRetrofitCallBack: RetrofitCallback<PDSResponseModule>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()

        try {
            jsonBody.put("pds", pdsNumber)


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
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.searchPDSPatient(
            "en",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(patientSearchRetrofitCallBack))
        return


    }


}