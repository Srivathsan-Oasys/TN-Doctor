package com.oasys.digihealth.doctor.ui.quick_reg.view_model

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
import com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.NewLmisOrderModule
import com.oasys.digihealth.doctor.ui.out_patient.model.search_request_model.SearchPatientRequestModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.*
import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.request.LabPrintReq
import com.oasys.digihealth.doctor.ui.quickregistration.model.PrintQuickRequest
import com.oasys.digihealth.doctor.ui.resultdispatch.request.Requestpdf
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject


class PDFViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {

    var progress = MutableLiveData<Int>()
    var errorText = MutableLiveData<String>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var facility_id: Int? = 0
    var appPreferences: AppPreferences? = null

    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        progress.value = 8
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
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

    fun GetPDFQuick(
        requestPDF: PrintQuickRequest,
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

        apiService?.getPDFQuick(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            requestPDF
        )?.enqueue(RetrofitMainCallback(GetPDFRetrofitCallback))

    }

    fun GetPDFdownload(
        requestpdf: Requestpdf,
        getPDFRetrofitCallback: RetrofitCallback<ResponseBody>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getresultPDF(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            requestpdf
        )?.enqueue(RetrofitMainCallback(getPDFRetrofitCallback))
    }


    fun dispatchPDFdownload(pdfid: Int, getPDFRetrofitCallback: RetrofitCallback<ResponseBody>) {


        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0

        val jsonBody = JSONObject()
        try {
            jsonBody.put("id", pdfid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getDispatchPDF(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(getPDFRetrofitCallback))

    }

    fun labPDFdownload(pdfid: Int, getPDFRetrofitCallback: RetrofitCallback<ResponseBody>) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0


        var req: LabPrintReq = LabPrintReq()

        var reqInt: ArrayList<Int> = ArrayList()

        reqInt.add(pdfid)


        req.Id = reqInt


        val jsonBody = JSONObject()
        try {
            jsonBody.put("Id", pdfid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getLabPDF(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            req
        )?.enqueue(RetrofitMainCallback(getPDFRetrofitCallback))

    }

    fun AdmittedPDFdownload(pdfid: Int, getPDFRetrofitCallback: RetrofitCallback<ResponseBody>) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0


        val jsonBody = JSONObject()
        try {
            jsonBody.put("admission_uuid", pdfid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getAdmissionPDF(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(getPDFRetrofitCallback))

    }

    fun rmisPDFdownload(pdfid: Int, getPDFRetrofitCallback: RetrofitCallback<ResponseBody>) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0


        var req: LabPrintReq = LabPrintReq()

        var reqInt: ArrayList<Int> = ArrayList()

        reqInt.add(pdfid)


        req.Id = reqInt


        val jsonBody = JSONObject()
        try {
            jsonBody.put("Id", pdfid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getRmisPDF(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            req
        )?.enqueue(RetrofitMainCallback(getPDFRetrofitCallback))

    }

    fun searchPatient(
        mobile: String,
        pinnumber: String,
        currentPage: Int,
        pageSize: Int,
        sortField: String,
        sortOrder: String,
        patientSearchRetrofitCallBack: RetrofitCallback<NewLmisOrderModule>
    ) {
        val searchPatientRequestModel = SearchPatientRequestModel()

        searchPatientRequestModel.mobile = mobile
        searchPatientRequestModel.pin = pinnumber
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

    fun printCasualtyPaySlip(
        facility_uuid: Int,
        printCasualtyPaySlipReq: PrintCasualtyPaySlipReq,
        printCasualtyPaySlipRespCallback: RetrofitCallback<ResponseBody>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.printCasualtyPaySlip(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            printCasualtyPaySlipReq
        )?.enqueue(RetrofitMainCallback(printCasualtyPaySlipRespCallback))
    }

}