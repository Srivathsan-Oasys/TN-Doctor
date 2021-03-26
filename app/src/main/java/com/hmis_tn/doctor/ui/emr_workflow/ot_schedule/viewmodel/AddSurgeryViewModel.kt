package com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitMainCallback
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.CreateEncounterRequestModel
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.Encounter
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_request.EncounterDoctor
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.op_notes.model.GetOpNotesEncounterByDocAndPatientIdResp
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.PharmacyDispenseResponse
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.delete.DeleteOtScheduleReq
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.delete.DeleteOtScheduleResp
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.modify.ModifyOtScheduleReq
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.modify.ModifyOtScheduleResp
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.response.OtNameSpinnerResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.response.OtTypeResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery.*
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery.save.AddSurgeryResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery.spinners.*
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.view.ViewOtScheduleReq
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.view.ViewOtScheduleResp
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class AddSurgeryViewModel(
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
    private var facility_UUID: Int? = 0

    init {
        errorTextVisibility.value = 8
        progressBar.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
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
            jsonBody.put("sortField", "uuid")

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


    fun getSurgeryPin(
        facilityid: Int?,
        otTypeRetrofitCallback: RetrofitCallback<GetSurgeryPinResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("pageNo", 1)
            jsonBody.put("paginationSize", 10)
            jsonBody.put("sortField", "created_date")
            jsonBody.put("sortOrder", "asc")
            jsonBody.put("pds", "")
            jsonBody.put("mobile", "")
            jsonBody.put("pin", "")
            jsonBody.put("searchKeyWord", "")

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
        apiService?.getSurgeryPin(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(otTypeRetrofitCallback))
    }


    fun getCheif(facilityid: Int?, cheifRetrofitCallback: RetrofitCallback<CheifResponseModel>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("user_type_uuid", 2)
            jsonBody.put("is_anaesthetist", 0)

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
        apiService?.getCheif(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(cheifRetrofitCallback))
    }

    //getSurgeon

    fun getSurgeon(
        facilityid: Int?,
        surgeonRetrofitCallback: RetrofitCallback<SurgeonResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("user_type_uuid", 26)
            jsonBody.put("is_anaesthetist", 0)

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
        apiService?.getSurgeon(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(surgeonRetrofitCallback))
    }

    //getNurse
    fun getNurse(
        facilityid: Int?,
        nurseRetrofitCallback: RetrofitCallback<NurseAssistantResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("user_type_uuid", 13)
            jsonBody.put("is_anaesthetist", 0)

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
        apiService?.getNurse(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(nurseRetrofitCallback))
    }


    fun getAnesthetist(
        facilityid: Int?,
        AnesthetistRetrofitCallback: RetrofitCallback<AnesthetistSpinnerResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("user_type_uuid", 2)
            jsonBody.put("is_anaesthetist", 1)


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
        apiService?.getAnesthetist(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(AnesthetistRetrofitCallback))
    }

    fun getLessionSpinner(
        facilityid: Int?,
        lessionRetrofitCallback: RetrofitCallback<LessionSpinnerResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("table_name", "lesions")
            jsonBody.put("sortField", "uuid")

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
        apiService?.getSpinnerLession(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(lessionRetrofitCallback))
    }

    fun getSpinnerSide(
        facilityid: Int?,
        sideRetrofitCallback: RetrofitCallback<SideSpinnerResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("table_name", "body_side")

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
        apiService?.getSpinnerSide(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(sideRetrofitCallback))
    }

    fun getAnaeesthesia(
        facilityid: Int?,
        anaeesthesiaRetrofitCallback: RetrofitCallback<AnaeesthesiaSpinnerResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("table_name", "anesthesia_type")

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
        apiService?.getAnaeesthesia(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(anaeesthesiaRetrofitCallback))
    }

    fun getPositions(
        facilityid: Int?,
        positionsRetrofitCallback: RetrofitCallback<PositionResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("table_name", "positions")

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
        apiService?.getPosition(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(positionsRetrofitCallback))
    }


    fun getPriority(
        facilityid: Int?,
        priorityRetrofitCallback: RetrofitCallback<PriorityResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("table_name", "ot_priority")
            jsonBody.put("sortField", "uuid")

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
        apiService?.getPriority(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(priorityRetrofitCallback))
    }

    fun getGrade(facilityid: Int?, gradeRetrofitCallback: RetrofitCallback<GradeResponseModel>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("table_name", "ot_grades")
            jsonBody.put("sortField", "uuid")

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
        apiService?.getGrade(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(gradeRetrofitCallback))
    }

    fun getSurgeryName(
        facilityid: Int?,
        query: String?,
        dept_id: Int?,
        surgerySearchRetrofitCallback: RetrofitCallback<SurgerySearchNameResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("facility_uuid", facilityid)
            jsonBody.put("department_uuid", dept_id)
            jsonBody.put("searchkey", query)
            jsonBody.put("sortField", "p_name")
            jsonBody.put("sortOrder", "ASC")
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
        apiService?.getSurgerySearchName(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(surgerySearchRetrofitCallback))
    }

    //getDiagnosis

    fun getDiagnosis(
        facilityid: Int?,
        query: String?,
        diagRetrofitCallback: RetrofitCallback<OtDiagnosisSearchResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("pageNo", 0)
            jsonBody.put("paginationSize", 10)
            jsonBody.put("searchValue", query)

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
        apiService?.getDiagnosis(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(diagRetrofitCallback))
    }


    fun addSurgery(
        facilityid: Int?,
        body: RequestBody,
        surgeryAddRetrofitCallback: RetrofitCallback<AddSurgeryResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progressBar.value = 0
        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()
        apiService?.addSurgery(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(surgeryAddRetrofitCallback))
    }

    fun viewOtSchedule(
        facilityid: Int?,
        viewOtScheduleReq: ViewOtScheduleReq,
        viewOtScheduleRespCallback: RetrofitCallback<ViewOtScheduleResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progressBar.value = 0
        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()
        apiService?.viewOtSchedule(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityid!!,
            viewOtScheduleReq

        )?.enqueue(RetrofitMainCallback(viewOtScheduleRespCallback))
    }

    fun modifyOtSchedule(
        facilityid: Int?,
        modifyOtScheduleReq: ModifyOtScheduleReq,
        modifyOtScheduleRespCallback: RetrofitCallback<ModifyOtScheduleResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progressBar.value = 0
        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()
        apiService?.modifyOtSchedule(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityid!!,
            modifyOtScheduleReq
        )?.enqueue(RetrofitMainCallback(modifyOtScheduleRespCallback))
    }

    fun deleteOtSchedule(
        facilityid: Int?,
        deleteOtScheduleReq: DeleteOtScheduleReq,
        deleteOtScheduleRespCallback: RetrofitCallback<DeleteOtScheduleResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progressBar.value = 0
        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()
        apiService?.deleteOtSchedule(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityid!!,
            deleteOtScheduleReq
        )?.enqueue(RetrofitMainCallback(deleteOtScheduleRespCallback))
    }

    fun getEncounter(
        facility_uuid: Int,
        patientId: Int,
        departmentId: Int,
        encounterType: Int,
        getOpNotesEncounterByDocAndPatientIdRespCallback: RetrofitCallback<GetOpNotesEncounterByDocAndPatientIdResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val doctorId = userDataStoreBean?.uuid

        apiService?.getOpNotesEncounter(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            patientId,
            doctorId!!,
            departmentId,
            encounterType
        )?.enqueue(RetrofitMainCallback(getOpNotesEncounterByDocAndPatientIdRespCallback))
    }

    fun createEncounter(
        patientUuid: Int,
        encounterType: Int,
        createEncounterRetrofitCallback: RetrofitCallback<CreateEncounterResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progressBar.value = 0
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
        encounter.encounter_type_uuid = encounterType
        encounter.facility_uuid = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        encounter.patient_uuid = patientUuid

        createEncounterRequestModel.encounter = encounter

        val encounterDoctor = EncounterDoctor()
        encounterDoctor.department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        encounterDoctor.dept_visit_type_uuid = encounterType
        encounterDoctor.doctor_uuid = userDataStoreBean?.uuid
        encounterDoctor.doctor_visit_type_uuid = encounterType
        encounterDoctor.patient_uuid = patientUuid
        encounterDoctor.session_type_uuid = 0
        encounterDoctor.speciality_uuid = 0
        encounterDoctor.sub_deparment_uuid = 0
        encounterDoctor.visit_type_uuid = encounterType

        createEncounterRequestModel.encounterDoctor = encounterDoctor

        apiService?.createEncounter(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            createEncounterRequestModel

        )!!.enqueue(
            RetrofitMainCallback(createEncounterRetrofitCallback)
        )
    }

    fun getPatientById(
        patient_id: Int,
        getPreviousDietOrderRespCallback: RetrofitCallback<PharmacyDispenseResponse>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("patientId", patient_id)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getPatientById(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_UUID!!,
            body
        )?.enqueue(RetrofitMainCallback(getPreviousDietOrderRespCallback))
    }
}