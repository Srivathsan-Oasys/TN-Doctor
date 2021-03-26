package com.hmis_tn.doctor.ui.emr_workflow.mrd.view_model


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitMainCallback
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.mrd.models.CaseSheetRequestModel
import com.hmis_tn.doctor.ui.emr_workflow.mrd.models.CaseSheetResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.mrd.models.MRDResponseModel
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject

class MRDViewModel(
    application: Application
) : AndroidViewModel(
    application
) {
    var errorText = MutableLiveData<String>()

    var progress = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var department_UUID: Int? = 0
    private var facility_id: Int? = 0

    var appPreferences: AppPreferences? = null

    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)

    }

    fun getMrd_Records(
        visitDate: String?,
        patientId: Int?,
        facilityid: Int?,
        encounter_uuid: Int?,
        encounter_type: Int?,
        mrdRetrofitCallback: RetrofitCallback<MRDResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        Log.i("", "" + patientId)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        progress.value = 0
        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()
        apiService?.getMrdList(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, patientId!!,
            encounter_uuid.toString(),
            encounter_type.toString(), visitDate
        )?.enqueue(RetrofitMainCallback(mrdRetrofitCallback))
    }

    fun getCaseSheet_Records(
        query: String,
        currentPage: Int,
        pageSize: Int,
        sortField: String,
        sortOrder: String,
        admittedPatientRetrofitCallback: RetrofitCallback<CaseSheetResponseModel>
    ) {

        val caseSheetRequestModel = CaseSheetRequestModel()
        if (query.length > 10) {
            caseSheetRequestModel.patient_mobile = ""
            caseSheetRequestModel.patient_pin = query
        } else {
            caseSheetRequestModel.patient_mobile = query
            caseSheetRequestModel.patient_pin = ""
        }
        caseSheetRequestModel.pageNo = currentPage
        caseSheetRequestModel.paginationSize = pageSize
        caseSheetRequestModel.sortField = sortField
        caseSheetRequestModel.sortOrder = sortOrder
        caseSheetRequestModel.is_emr = true
        caseSheetRequestModel.visit_type = ""


        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getcasesheetsummary(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            appPreferences?.getInt(AppConstants.FACILITY_UUID)!!,
            caseSheetRequestModel
        )!!.enqueue(
            RetrofitMainCallback(admittedPatientRetrofitCallback)
        )

    }

    fun getPatientListNextPage(
        query: String,
        currentPage: Int,
        pageSize: Int,
        sortField: String,
        sortOrder: String,
        admittedPatientRetrofitCallback: RetrofitCallback<CaseSheetResponseModel>
    ) {


        val caseSheetRequestModel = CaseSheetRequestModel()
        if (query.length > 10) {
            caseSheetRequestModel.patient_mobile = ""
            caseSheetRequestModel.patient_pin = query
        } else {
            caseSheetRequestModel.patient_mobile = query
            caseSheetRequestModel.patient_pin = ""
        }
        caseSheetRequestModel.pageNo = currentPage
        caseSheetRequestModel.paginationSize = pageSize
        caseSheetRequestModel.sortField = sortField
        caseSheetRequestModel.sortOrder = sortOrder
        caseSheetRequestModel.is_emr = true
        caseSheetRequestModel.visit_type = ""


        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getcasesheetsummary(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            appPreferences?.getInt(AppConstants.FACILITY_UUID)!!,
            caseSheetRequestModel
        )!!.enqueue(
            RetrofitMainCallback(admittedPatientRetrofitCallback)
        )
    }


    fun GetMrdPDFf(
        patientId: Int?,
        facilityid: Int?,
        encounter_uuid: Int?,
        encounter_type: Int?, GetPDFRetrofitCallback: RetrofitCallback<ResponseBody>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        val jsonBody = JSONObject()
        try {
            jsonBody.put("patient_uuid", patientId)
            jsonBody.put("encounter_uuid", encounter_uuid)
            jsonBody.put("encounter_type_uuid", encounter_type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        apiService?.getMrdPrintList(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(GetPDFRetrofitCallback))

    }


}