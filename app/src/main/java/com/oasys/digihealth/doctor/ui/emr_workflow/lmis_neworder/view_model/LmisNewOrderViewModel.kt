package com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.view_model

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
import com.oasys.digihealth.doctor.ui.emr_workflow.delete.model.DeleteResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.gettemplate.ResponseLabGetTemplateDetails
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.request.LabTechSearch
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.request.RequestLmisNewOrder
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.response.GetWardIdResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.response.ResponseLmisListview
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EMR_Response.EmrResponceModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.CreateEncounterRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.Encounter
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_request.EncounterDoctor
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.templete.TempleResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.GetToLocationTestResponse
import com.oasys.digihealth.doctor.ui.out_patient.model.search_request_model.SearchPatientRequestModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.GetReferenceResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.LocationMasterResponseModelX
import com.oasys.digihealth.doctor.ui.quick_reg.model.ResponseTestMethod
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response.LabTestSpinnerResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class LmisNewOrderViewModel(
    application: Application
) : AndroidViewModel(
    application
) {
    var errorText = MutableLiveData<String>()

    var progress = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var department_UUID: Int? = 0
    private var facility_id: Int? = 0

    private var labUuid: Int? = null

    private var encountertype: Int? = null

    private var otherDepartment: String = ""

    var appPreferences: AppPreferences? = null

    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        labUuid = appPreferences?.getInt(AppConstants.LAB_UUID)

        encountertype = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
        otherDepartment = appPreferences?.getString(AppConstants.OTHER_DEPARTMENT_UUID)!!
    }

    fun searchPatient(
        InputPutFieldInput: String,
        currentPage: Int,
        pageSize: Int,
        sortField: String,
        sortOrder: String,
        patientSearchRetrofitCallBack: RetrofitCallback<NewLmisOrderModule>
    ) {
        val searchPatientRequestModel = SearchPatientRequestModel()
        if (InputPutFieldInput.length > 10) {
            searchPatientRequestModel.mobile = ""
            searchPatientRequestModel.pin = InputPutFieldInput
        } else {
            searchPatientRequestModel.mobile = InputPutFieldInput
            searchPatientRequestModel.pin = ""
        }

        searchPatientRequestModel.pageNo = currentPage
        searchPatientRequestModel.paginationSize = pageSize
        searchPatientRequestModel.sortField = sortField
        searchPatientRequestModel.sortOrder = sortOrder

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.searchOutPatientLmisOrder(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            appPreferences?.getInt(AppConstants.FACILITY_UUID)!!,
            searchPatientRequestModel
        )!!.enqueue(
            RetrofitMainCallback(patientSearchRetrofitCallBack)
        )
    }

    fun getPatientListNextPage(
        InputPutFieldInput: String,
        currentPage: Int,
        pageSize: Int,
        patientSearchNextRetrofitCallBack: RetrofitCallback<NewLmisOrderModule>
    ) {

        val searchPatientRequestModel = SearchPatientRequestModel()

        if (InputPutFieldInput.length > 10) {
            searchPatientRequestModel.mobile = ""
            searchPatientRequestModel.pin = InputPutFieldInput
        } else {
            searchPatientRequestModel.mobile = InputPutFieldInput
            searchPatientRequestModel.pin = ""
        }
        searchPatientRequestModel.pageNo = currentPage
        searchPatientRequestModel.paginationSize = pageSize
        searchPatientRequestModel.sortField = "modified_date"
        searchPatientRequestModel.sortOrder = "DESC"

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.searchOutPatientLmisOrder(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            appPreferences?.getInt(AppConstants.FACILITY_UUID)!!,
            searchPatientRequestModel
        )!!.enqueue(
            RetrofitMainCallback(patientSearchNextRetrofitCallBack)
        )
    }

    fun getFavourites(
        emrWorkFlowRetrofitCallBack: RetrofitCallback<FavouritesResponseModel>,
        labid: Int?
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getLmisFavourites(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!,
            labid!!,
            AppConstants.FAV_TYPE_ID_LAB
        )?.enqueue(RetrofitMainCallback(emrWorkFlowRetrofitCallBack))

        /*if(department_UUID!=0){

            apiService?.getLmisFavouritesDept(AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,facility_id!!, department_UUID!!,
                AppConstants.FAV_TYPE_ID_LAB
            )?.enqueue(RetrofitMainCallback(emrWorkFlowRetrofitCallBack))

        }
        else{



        }*/


        return
    }

    fun getTemplete(templeteRetrofitCallBack: RetrofitCallback<TempleResponseModel>, labid: Int?) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getLmisTemplete(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!,
            labid!!,
            AppConstants.FAV_TYPE_ID_LAB
        )?.enqueue(RetrofitMainCallback(templeteRetrofitCallBack))

        /*   if(department_UUID!=0){
               apiService?.getLmisTemplete(AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, userDataStoreBean?.uuid!!, department_UUID!!,facility_id!!,
                   AppConstants.FAV_TYPE_ID_LAB
               )?.enqueue(RetrofitMainCallback(templeteRetrofitCallBack))

           }
           else{



           }*/


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
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
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
        )?.enqueue(RetrofitMainCallback(favAddAllDepartmentCallBack))
        return

    }

    fun getuserDepartment(
        doctorUUId: Int?,
        favAddAllDepartmentCallBack: RetrofitCallback<FavAddAllDepatResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        try {
            jsonBody.put("doctor_uuid", doctorUUId)
            jsonBody.put("is_lab", true)
            jsonBody.put("paginationSize", 100000)
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

        apiService?.getUserDepartment(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(favAddAllDepartmentCallBack))
        return

    }


    fun getMethod(
        tablename: String,
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
            jsonBody.put("table_name", tablename)
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
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(ResponseTestMethodRetrofitCallback))

    }

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

        apiService?.getLocation(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, body
        )?.enqueue(RetrofitMainCallback(stateRetrofitCallback))

        return
    }

    fun getTextMethod1(
        search: String,
        ResponseTestMethodRetrofitCallback: RetrofitCallback<LabTestSpinnerResponseModel>
    ) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }


        val req: LabTechSearch = LabTechSearch()

        if (labUuid != 0) {
            req.lab_uuid = labUuid.toString()
        }
        req.sortField = "uuid"
        req.sortOrder = "DESC"
        req.search = search
        req.is_default = false

        if (otherDepartment != "") {

            otherDepartment = otherDepartment.replace("[", "")

            otherDepartment = otherDepartment.replace("]", "")

            val dept = Utils.stringToIntArray(otherDepartment)

            req.other_department_uuids = dept


            progress.value = 0
            val aiiceApplication = HmisApplication.get(getApplication())
            val apiService = aiiceApplication.getRetrofitService()

            apiService?.getLabTestSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, true,
                req
            )?.enqueue(RetrofitMainCallback(ResponseTestMethodRetrofitCallback))

        } else {

            try {
                if (labUuid != 0) {

                    jsonBody.put("lab_uuid", labUuid)

                } else {

                    jsonBody.put("lab_uuid", null)

                }
                jsonBody.put("sortField", "uuid")
                jsonBody.put("sortOrder", "DESC")
                jsonBody.put("search", search)
                jsonBody.put("is_default", false)


                if (otherDepartment != "") {
                    val dept = Utils.stringToIntArray(otherDepartment)

                    jsonBody.put("other_department_uuids", dept)

                } else {


                }


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

            apiService?.getLabTestSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, true,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestMethodRetrofitCallback))


        }


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

    fun getPrevLabCallback(
        patientId: Int?,
        facilityid: Int?,
        historyRadiologyRetrofitCallback: RetrofitCallback<PrevLabLmisResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("patient_id", patientId)
            jsonBody.put("lab_master_type_uuid", AppConstants.LAB_TESTMASTER_UUID)

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
        apiService?.getPrevLmisLab(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(historyRadiologyRetrofitCallback))
    }


    fun deleteFavourite(
        facility_id: Int?,
        favouriteId: Int?,
        deleteRetrofitCallback: RetrofitCallback<DeleteResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("favouriteId", favouriteId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.deleteRows(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, body
        )?.enqueue(RetrofitMainCallback(deleteRetrofitCallback))
        return
    }

    fun deleteTemplate(
        facility_id: Int?,
        template_uuid: Int?,
        deleteRetrofitCallback: RetrofitCallback<DeleteResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("template_uuid", template_uuid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.deleteTemplate(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, body
        )?.enqueue(RetrofitMainCallback(deleteRetrofitCallback))
        return
    }

    fun getTemplateDetails(
        templateId: Int?,
        facilityUuid: Int?,
        LabID: Int?,
        getTemplateRetrofitCallback: RetrofitCallback<ResponseLabGetTemplateDetails>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        if (department_UUID != 0) {
            apiService?.getLastTemplate(
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,
                facilityUuid!!,
                templateId!!,
                AppConstants.FAV_TYPE_ID_LAB,
                department_UUID!!
            )?.enqueue(RetrofitMainCallback(getTemplateRetrofitCallback))
        } else {
            apiService?.getLmisLastTemplate(
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,
                facilityUuid!!,
                templateId!!,
                AppConstants.FAV_TYPE_ID_LAB,
                LabID!!
            )?.enqueue(RetrofitMainCallback(getTemplateRetrofitCallback))
        }

        return
    }

    fun labInsert(
        facility_id: Int?,
        EmrRequestData: RequestLmisNewOrder,
        configFinalRetrofitCallBack: RetrofitCallback<EmrResponceModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.lmisEmrpost(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            EmrRequestData
        )?.enqueue(RetrofitMainCallback(configFinalRetrofitCallBack))
        return

    }


    fun updateLab(
        facility_id: Int?,
        labModifiyRequest: LabModifiyRequest,
        configFinalRetrofitCallBack: RetrofitCallback<LabModifiyResponse>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.lmisLabUpdate(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            labModifiyRequest
        )?.enqueue(RetrofitMainCallback(configFinalRetrofitCallBack))
        return

    }


    fun getComplaintSearchResult(
        facility_id: Int?,
        name: String?,
        complaintSearchRetrofitCallBack: RetrofitCallback<ResponseLmisListview>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("sortField", "uuid")
            jsonBody.put("sortOrder", "DESC")
            jsonBody.put("search", name)
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
        apiService?.getLmisSearchResult(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(complaintSearchRetrofitCallBack))
        return
    }

    fun createEncounter(
        patientUuid: Int,
        encounterType: Int,
        doctorId: Int, departmentID: Int?,
        createEncounterCallback: RetrofitCallback<CreateEncounterResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        val createEncounterRequestModel = CreateEncounterRequestModel()

        val encounter = Encounter()
        encounter.admission_request_uuid = 0
        encounter.admission_uuid = 0
        encounter.appointment_uuid = 0
        encounter.department_uuid = departmentID
        encounter.discharge_type_uuid = 0
        encounter.encounter_identifier = 0
        encounter.encounter_priority_uuid = 0
        encounter.encounter_status_uuid = 0
        encounter.encounter_type_uuid = encounterType
        encounter.facility_uuid = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        encounter.patient_uuid = patientUuid

        createEncounterRequestModel.encounter = encounter

        val encounterDoctor = EncounterDoctor()
        encounterDoctor.department_uuid = departmentID
        encounterDoctor.dept_visit_type_uuid = encounterType
        encounterDoctor.doctor_uuid = doctorId
        encounterDoctor.doctor_visit_type_uuid = encounterType
        encounterDoctor.patient_uuid = patientUuid
        encounterDoctor.session_type_uuid = 0
        encounterDoctor.speciality_uuid = 0
        encounterDoctor.sub_deparment_uuid = 0
        encounterDoctor.visit_type_uuid = encounterType

        createEncounterRequestModel.encounterDoctor = encounterDoctor

        apiService?.LmiscreateEncounter(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            facility_id!!,
            userDataStoreBean?.uuid!!,
            createEncounterRequestModel

        )!!.enqueue(
            RetrofitMainCallback(createEncounterCallback)
        )
    }

    fun getDoctorName(
        facilityId: Int,
        doctorNameCallback: RetrofitCallback<DoctorNameResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("facility_uuid", facilityId)
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
        val userDataStoreBean = userDetailsRoomRepository!!.getUserDetails()

        apiService?.getDoctorName(
            "en",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId, body

        )?.enqueue(RetrofitMainCallback(doctorNameCallback))
    }

    fun getEncounter(
        facility_id: Int?,
        patientUuid: Int,
        doctorId: Int,
        encounterType: Int, departmentID: Int?,
        fetchEncounterRetrofitCallBack: RetrofitCallback<FectchEncounterResponseModel>
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
        apiService?.getLmisEncounters(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            facility_id!!,
            userDataStoreBean?.uuid!!,
            patientUuid,
            doctorId,
            departmentID!!,
            encounterType

        )!!.enqueue(
            RetrofitMainCallback(fetchEncounterRetrofitCallBack)
        )
    }


    fun getDepartmentList(
        facilityID: Int?,
        departmentRetrofitCallBack: RetrofitCallback<FavAddResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("uuid", department_UUID)
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
            userDataStoreBean?.uuid!!, facilityID!!,
            body
        )?.enqueue(RetrofitMainCallback(departmentRetrofitCallBack))
        return
    }

    fun getWardId(
        patientId: Int,
        wardIdRetrofitCallback: RetrofitCallback<GetWardIdResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getWardIdByPatientId(
            AppConstants.LANGUAGE,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            patientId
        )?.enqueue(RetrofitMainCallback(wardIdRetrofitCallback))
        return

    }

    fun getToLocationTest(
        labToLoctionTestRetrofitCallback: RetrofitCallback<GetToLocationTestResponse>,
        facilityId: Int,
        strAutoId: Int?,
        id: Int,
        wardUUid: Int
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("test_master_uuid", id)
            jsonBody.put("from_department_uuid", strAutoId)
            jsonBody.put("profile_uuid", null)


            if (encountertype == AppConstants.TYPE_IN_PATIENT) {

                jsonBody.put("ward_uuid", wardUUid)


            }


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
        apiService?.getToLocationLabTest(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!, body
        )?.enqueue(RetrofitMainCallback(labToLoctionTestRetrofitCallback))
        return

    }

}