package com.hmis_tn.doctor.ui.emr_workflow.documents.view_model

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
import com.hmis_tn.doctor.ui.emr_workflow.documents.model.*
import com.hmis_tn.doctor.utils.Utils
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject


class DocumentViewModel(
    application: Application
) : AndroidViewModel(
    application
) {
    private var department_UUID: Int? = 0
    private var patiend_UUID: Int? = 0
    var errorTextVisibility = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var appPreferences: AppPreferences? = null
    var errorText = MutableLiveData<String>()

    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        patiend_UUID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
    }


    fun getDocumentTypeList(
        facilityId: Int,
        documentTypeResponseCallback: RetrofitCallback<DocumentTypeResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorTextVisibility.value =
                getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getDocumentType(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId
        )?.enqueue(RetrofitMainCallback(documentTypeResponseCallback))
        return
    }

    fun getAddDocumentTypeList(
        facilityId: Int,
        addDocumentTypeResponseCallback: RetrofitCallback<AddDocumentDetailsResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorTextVisibility.value =
                getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getAddDocumentDetails(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId, patiend_UUID!!
        )?.enqueue(RetrofitMainCallback(addDocumentTypeResponseCallback))
        return
    }

    fun deleteAttachments(
        facility_id: Int?,
        attachmentId: Int?,
        deleteRetrofitCallback: RetrofitCallback<DeleteDocumentResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorTextVisibility.value =
                getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.deleteAttachmentsRows(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, attachmentId!!
        )?.enqueue(RetrofitMainCallback(deleteRetrofitCallback))
        return
    }

    fun getDownload(
        documentId: Attachment,
        facilityId: Int?,
        downloadfile: RetrofitCallback<ResponseBody>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("filePath", documentId.file_path)
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
        apiService?.getDownload(
            "en",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityId!!, body
        )?.enqueue(RetrofitMainCallback(downloadfile))

    }

    fun getUploadFile(
        facilityId: Int,
        body: MultipartBody.Part,
        foldername: String,
        attachedDate: String,
        attachmentTypeUuid: String,
        comments: String,
        attachedname: String,
        encounterUuid: Int?,
        patientUuid: Int?,
        adduploadCallback: RetrofitCallback<FileUploadResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        // create RequestBody instance from file
        val multipartfoldername = RequestBody.create(
            MultipartBody.FORM, foldername
        )
        val multipartattachedDate = RequestBody.create(
            MultipartBody.FORM, attachedDate
        )
        val multipartpatientUuid = RequestBody.create(
            MultipartBody.FORM, patientUuid.toString()
        )
        val multipartencounter = RequestBody.create(
            MultipartBody.FORM, encounterUuid.toString()
        )
        val multipartattachmentTypeUuid = RequestBody.create(
            MultipartBody.FORM, attachmentTypeUuid
        )
        val multipartcomments = RequestBody.create(
            MultipartBody.FORM, comments
        )
        val multipartattachedname = RequestBody.create(
            MultipartBody.FORM, attachedname
        )
        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()
        apiService?.getUploadFile(
            "en",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId,
            body,
            multipartfoldername,
            multipartattachedDate,
            multipartpatientUuid,
            multipartencounter,
            multipartattachmentTypeUuid,
            multipartcomments,
            multipartattachedname
        )?.enqueue(RetrofitMainCallback(adduploadCallback))
    }
}