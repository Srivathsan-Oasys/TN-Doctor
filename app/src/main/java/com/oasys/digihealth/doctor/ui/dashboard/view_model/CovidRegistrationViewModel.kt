package com.oasys.digihealth.doctor.ui.dashboard.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.application.HmisApplication
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.ui.covid.addpatientrequest.*
import com.oasys.digihealth.doctor.ui.covid.addpatientresponse.AddPatientResponse
import com.oasys.digihealth.doctor.ui.dashboard.model.*
import com.oasys.digihealth.doctor.ui.dashboard.model.registration.*
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject


class CovidRegistrationViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {


    var enterOTPEditText = MutableLiveData<String>()
    var enterNewPasswordEditText = MutableLiveData<String>()
    var enterConfirmPasswordEditText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var errorText = MutableLiveData<String>()

    var progressBar = MutableLiveData<Int>()

    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    var facility_id: Int? = 0

    var appPreferences: AppPreferences? = null


    init {
        enterOTPEditText.value = ""
        enterNewPasswordEditText.value = ""
        enterConfirmPasswordEditText.value = ""
        progressBar.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)

        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)

        progress.value = 8
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
    }


    /*
    SpecimanTYPE
     */
/*
SpecimanTYPE
*/
    fun getSpecimanType(
        facilityId: Int?,
        specimenListRetrofitCallBack: RetrofitCallback<ResponseSpicemanType>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()


        apiService?.getSpecimen_Type(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, AppConstants.ACCEPT_LANGUAGE_EN,
            facilityId!!, "specimen_type"
        ).enqueue(RetrofitMainCallback(specimenListRetrofitCallBack))
        return
    }


    fun getCovidMobileBelongsToList(
        query: String,
        facility_uuid: Int,
        getMobileBelongsToGenderRetrofitCallBack: RetrofitCallback<CovidMobileBelongsToResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getMobileBelongsTo(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            query
        ).enqueue(RetrofitMainCallback(getMobileBelongsToGenderRetrofitCallBack))
        return
    }

    fun getCovidPatientCategoryList(
        query: String,
        facility_uuid: Int,
        getPatientCategoryRetrofitCallBack: RetrofitCallback<CovidPatientCategoryResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getPatientCategory(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            query
        ).enqueue(RetrofitMainCallback(getPatientCategoryRetrofitCallBack))
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
        progressBar.value = 0
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
        progressBar.value = 0
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

    fun getCovidNationalityList(
        query: String,
        facilityId: Int,
        covidNationalityResponseCallback: RetrofitCallback<CovidNationalityResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getNationality(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId,
            query
        ).enqueue(RetrofitMainCallback(covidNationalityResponseCallback))
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
        progressBar.value = 0
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


    fun getStateList(stateRetrofitCallback: RetrofitCallback<StateListResponseModel>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("country_uuid", 1)
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

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getDistict(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        ).enqueue(RetrofitMainCallback(distictRetrofitCallback))


        return

    }

    fun getDistrict1(
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

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getDistict(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        ).enqueue(RetrofitMainCallback(distictRetrofitCallback))


        return

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

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getTaluk(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        ).enqueue(RetrofitMainCallback(talukRetrofitCallback))

    }

    fun getTaluk1(uuid: Int, talukRetrofitCallback: RetrofitCallback<TalukListResponseModel>) {

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

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getTaluk(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        ).enqueue(RetrofitMainCallback(talukRetrofitCallback))

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

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getVillage(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        ).enqueue(RetrofitMainCallback(talukRetrofitCallback))

    }

    fun getVillage1(uuid: Int, talukRetrofitCallback: RetrofitCallback<VilliageListResponceModel>) {


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

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getVillage(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        ).enqueue(RetrofitMainCallback(talukRetrofitCallback))

    }

    fun getOutcome(specimenListRetrofitCallBack: RetrofitCallback<ResponseSpicemanType>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()


        apiService?.getSpecimen_Type(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, AppConstants.ACCEPT_LANGUAGE_EN,
            facility_id!!, "out_come_type"
        ).enqueue(RetrofitMainCallback(specimenListRetrofitCallBack))
        return
    }

    fun getConditionType(
        facilityId: Int?,
        specimenListRetrofitCallBack: RetrofitCallback<ResponseSpicemanType>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()


        apiService?.getSpecimen_Type(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, AppConstants.ACCEPT_LANGUAGE_EN,
            facilityId!!, "underline_medicine_condition_type"
        ).enqueue(RetrofitMainCallback(specimenListRetrofitCallBack))
        return
    }

    fun getSymptoms(
        facilityId: Int?,
        specimenListRetrofitCallBack: RetrofitCallback<ResponseSpicemanType>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()


        apiService?.getSpecimen_Type(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, AppConstants.ACCEPT_LANGUAGE_EN,
            facilityId!!, "symptoms"
        ).enqueue(RetrofitMainCallback(specimenListRetrofitCallBack))
        return
    }

    fun addpatientDetails(
        facilityId: Int?,
        AddPatientReqestBody: AddPatientDetailsRequest,
        addPatientDetailsResponseCallback: RetrofitCallback<AddPatientResponse>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getAddPatientDetails(
            AppConstants?.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId!!,
            AddPatientReqestBody
        ).enqueue(RetrofitMainCallback(addPatientDetailsResponseCallback))
        return
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

    fun getAllSearchCovidPatientList(
        Str_mobileNumber: String?,
        strPinnumber: String?,
        PatientNameRetrofitCallback: RetrofitCallback<CovidRegistrationSearchResponseModel>,
        strName: String?
    ) {
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {
            jsonBody.put("mobile", Str_mobileNumber)
            jsonBody.put("searchKeyWord", strName)
            jsonBody.put("pin", strPinnumber)

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

    fun getCovidQuarantineTypeList(
        facilityId: Int,
        covidQuarantineResponseCallback: RetrofitCallback<CovidQuarantineTypeResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getQuarantineType(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId
        ).enqueue(RetrofitMainCallback(covidQuarantineResponseCallback))
        return

    }

    fun getTestRequestedBy(specimenListRetrofitCallBack: RetrofitCallback<ResponseSpicemanType>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()


        apiService?.getSpecimen_Type(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, AppConstants.ACCEPT_LANGUAGE_EN,
            facility_id!!, "test_request_type"
        ).enqueue(RetrofitMainCallback(specimenListRetrofitCallBack))
        return
    }

    fun updatepatientDetails(
        facilityId: Int?,
        AddPatientReqestBody: UpdatePatientDetailsRequest,
        addPatientDetailsResponseCallback: RetrofitCallback<AddPatientResponse>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getUpdatePatientDetails(
            AppConstants?.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId!!,
            AddPatientReqestBody
        ).enqueue(RetrofitMainCallback(addPatientDetailsResponseCallback))
        return
    }

    fun getRepeatedResult(
        facilityId: Int,
        covidRepeatedResultResponseCallback: RetrofitCallback<RepeatedResultResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getRepeatedResult(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId
        ).enqueue(RetrofitMainCallback(covidRepeatedResultResponseCallback))
        return
    }

    //getIntervals
    fun getIntervals(
        facilityId: Int,
        covidIntervalsResponseCallback: RetrofitCallback<RepeatedIntervalReponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getIntervals(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId
        ).enqueue(RetrofitMainCallback(covidIntervalsResponseCallback))
        return
    }

    fun getSpecimenDetails(
        pataint_id: String,
        covidIntervalsResponseCallback: RetrofitCallback<specimenResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getspecimenDetails(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            this!!.facility_id!!, "patient_specimen_details", pataint_id
        ).enqueue(RetrofitMainCallback(covidIntervalsResponseCallback))
        return
    }


    fun getSymptomsDetails(
        pataint_id: String,
        covidIntervalsResponseCallback: RetrofitCallback<symptomResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getsymptomsDetails(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            this!!.facility_id!!, "patient_symptoms", pataint_id
        ).enqueue(RetrofitMainCallback(covidIntervalsResponseCallback))
        return
    }

    fun getPatientsDetails(
        pataint_id: String,
        covidIntervalsResponseCallback: RetrofitCallback<CovidRegisterPatientResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getpatientDetails(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            this!!.facility_id!!, "covid_patient_details", pataint_id
        ).enqueue(RetrofitMainCallback(covidIntervalsResponseCallback))
        return
    }

    fun getConditionDetails(
        pataint_id: String,
        covidIntervalsResponseCallback: RetrofitCallback<ConditionDetailsResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getconditionDetails(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            this!!.facility_id!!, "patient_condition_details", pataint_id
        ).enqueue(RetrofitMainCallback(covidIntervalsResponseCallback))
        return
    }


}