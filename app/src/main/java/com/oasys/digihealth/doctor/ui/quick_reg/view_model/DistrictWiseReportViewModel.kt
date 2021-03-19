package com.oasys.digihealth.doctor.ui.quick_reg.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.application.HmisApplication
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.ui.quick_reg.model.reports.requset.LabWiseReportRequestModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.reports.response.LabConsolidatedReportLabelResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.reports.response.LabFilterResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.reports.response.LabWiseReportLabelResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.reports.response.LabWiseReportResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class DistrictWiseReportViewModel(application: Application?)
    : AndroidViewModel(application!!){

    var progress = MutableLiveData<Int>()
    var errorText = MutableLiveData<String>()

    var facility_id:Int?=0

    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    init {

        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)

        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)

        //progress.value = 8
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
    }


    //op services
    fun getDistPatientCountTable(toggle: String,labWiseReportRequestModel: LabWiseReportRequestModel, labWiseResponseSecondRetrofitCallback: RetrofitCallback<LabWiseReportResponseModel>) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        if(toggle.equals("OP")){
            apiService?.getDistPatientCountTable(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,
                facility_id!!, userDataStoreBean?.user_name,
                labWiseReportRequestModel
            )?.enqueue(RetrofitMainCallback(labWiseResponseSecondRetrofitCallback))

        }else{
            apiService?.getDistPatientCountTableIp(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,
                facility_id!!, userDataStoreBean?.user_name,
                labWiseReportRequestModel
            )?.enqueue(RetrofitMainCallback(labWiseResponseSecondRetrofitCallback))

        }


    }
    fun getDistPatientCountLabel(toggle: String,requestLabWiseReportListRequest: LabWiseReportRequestModel, GetLabWiseListRetrofitCallback: RetrofitCallback<LabWiseReportLabelResponseModel>
    ) {

        Log.e("getList fun","inside")
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        if(toggle.equals("OP")){
            apiService?.getDistPatientCountLabel(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,facility_id!!, userDataStoreBean?.user_name,
                requestLabWiseReportListRequest
            )?.enqueue(RetrofitMainCallback(GetLabWiseListRetrofitCallback))

        }else{
            apiService?.getDistPatientCountLabelIp(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,facility_id!!, userDataStoreBean?.user_name,
                requestLabWiseReportListRequest
            )?.enqueue(RetrofitMainCallback(GetLabWiseListRetrofitCallback))

        }

    }


    fun getDistrictHud(toggle: String,district_id: MutableList<Int>, ResponseTestAssignedToRetrofitCallback: RetrofitCallback<LabFilterResponseModel>) {
        val districtID = JSONArray()
        for(item in district_id){
            districtID.put(item)
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }


        try {

            jsonBody.put("user_Id", userDataStoreBean?.uuid!!)
            jsonBody.putOpt("district_Id",districtID)

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

        if(toggle.equals("OP")){
            apiService?.getDistHUDSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,facility_id!!, userDataStoreBean?.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }else{
            apiService?.getDistHUDSpinnerIp(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,facility_id!!, userDataStoreBean?.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }
    fun getDistrictBlock(toggle: String,district_id: MutableList<Int>,hud_id: MutableList<Int>, ResponseTestAssignedToRetrofitCallback: RetrofitCallback<LabFilterResponseModel>) {
        val districtID = JSONArray()
        for(item in district_id){
            districtID.put(item)
        }
        val hudID = JSONArray()
        for(item in hud_id){
            hudID.put(item)
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {

            jsonBody.put("user_Id", userDataStoreBean?.uuid!!)
            jsonBody.put("district_Id", districtID)
            jsonBody.put("hud_Id", hudID)

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

        if(toggle.equals("OP")){
            apiService?.getDistBlockSpinner(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,facility_id!!, userDataStoreBean?.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }else{
            apiService?.getDistBlockSpinnerIp(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,facility_id!!, userDataStoreBean?.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }
    fun getDistrictOffice(toggle: String,district_id: MutableList<Int>,hud_id: MutableList<Int>,block_id: MutableList<Int>, ResponseTestAssignedToRetrofitCallback: RetrofitCallback<LabFilterResponseModel>) {
        val districtID = JSONArray()
        for(item in district_id){
            districtID.put(item)
        }
        val hudID = JSONArray()
        for(item in hud_id){
            hudID.put(item)
        }
        val blockID = JSONArray()
        for(item in block_id){
            blockID.put(item)
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {

            jsonBody.put("user_Id", userDataStoreBean?.uuid!!)
            jsonBody.put("district_Id", districtID)
            jsonBody.put("hud_Id", hudID)
            jsonBody.put("block_Id", blockID)

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

        if(toggle.equals("OP")){
            apiService?.getDistOfficeSpinner(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,facility_id!!, userDataStoreBean?.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }else{
            apiService?.getDistOfficeSpinnerIp(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,facility_id!!, userDataStoreBean?.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }

    fun getLabDistrictReportListSecond(toggle: String,labWiseReportRequestModel: LabWiseReportRequestModel, labWiseResponseSecondRetrofitCallback: RetrofitCallback<LabWiseReportResponseModel>) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        if(toggle.equals("OP")){
            apiService?.getDistPatientCountTable(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,facility_id!!, userDataStoreBean?.user_name,
                labWiseReportRequestModel
            )?.enqueue(RetrofitMainCallback(labWiseResponseSecondRetrofitCallback))

        }else{
            apiService?.getDistPatientCountTableIp(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,facility_id!!, userDataStoreBean?.user_name,
                labWiseReportRequestModel
            )?.enqueue(RetrofitMainCallback(labWiseResponseSecondRetrofitCallback))


        }

    }

    fun getDistrictDropDown(toggle: String, ResponseDistrictRetrofitCallback: RetrofitCallback<LabFilterResponseModel>) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        Log.i("dataaaaa",userDataStoreBean.toString())
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {

            jsonBody.put("user_Id", userDataStoreBean?.uuid!!)

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

        if(toggle.equals("OP")){
            apiService?.getDistDropDownSpinner(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,facility_id!!, userDataStoreBean?.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseDistrictRetrofitCallback))

        }else{
            apiService?.getDistDropDownSpinnerIp(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,facility_id!!, userDataStoreBean?.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseDistrictRetrofitCallback))

        }

    }

    fun getDistrictInstitutionType(toggle: String,district_id: MutableList<Int>,hud_id: MutableList<Int>,block_id: MutableList<Int>,office_id: MutableList<Int>, ResponseTestAssignedToRetrofitCallback: RetrofitCallback<LabFilterResponseModel>) {

        val districtID = JSONArray()
        for(item in district_id){
            districtID.put(item)
        }
        val hudID = JSONArray()
        for(item in hud_id){
            hudID.put(item)
        }
        val blockID = JSONArray()
        for(item in block_id){
            blockID.put(item)
        }
        val officeID = JSONArray()
        for(item in office_id){
            officeID.put(item)
        }

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {

            jsonBody.put("user_Id", userDataStoreBean?.uuid!!)
            jsonBody.put("district_Id", districtID)
            jsonBody.put("hud_Id", hudID)
            jsonBody.put("block_Id", blockID)
            jsonBody.put("office_Id", officeID)

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
        if(toggle.equals("OP")){
            apiService?.getDistInstitutionTypeSpinner(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,facility_id!!, userDataStoreBean?.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }else{
            apiService?.getDistInstitutionTypeSpinnerIp(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,facility_id!!, userDataStoreBean?.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }


    }


    fun getDistrictInstitution(toggle: String,district_id: MutableList<Int>,hud_id: MutableList<Int>,block_id: MutableList<Int>,office_id: MutableList<Int>,institutionType_id: MutableList<Int>, ResponseTestAssignedToRetrofitCallback: RetrofitCallback<LabFilterResponseModel>) {
        val districtID = JSONArray()
        for(item in district_id){
            districtID.put(item)
        }
        val hudID = JSONArray()
        for(item in hud_id){
            hudID.put(item)
        }
        val blockID = JSONArray()
        for(item in block_id){
            blockID.put(item)
        }
        val officeID = JSONArray()
        for(item in office_id){
            officeID.put(item)
        }
        val institutionTypeID = JSONArray()
        for(item in institutionType_id){
            institutionTypeID.put(item)
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {

            jsonBody.put("user_Id", userDataStoreBean?.uuid!!)
            jsonBody.put("district_Id", districtID)
            jsonBody.put("hud_Id", hudID)
            jsonBody.put("block_Id", blockID)
            jsonBody.put("office_Id", officeID)
            jsonBody.put("institutiontype_Id", institutionTypeID)

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

        if(toggle.equals("OP")){
            apiService?.getDistInstitutionSpinner(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,facility_id!!, userDataStoreBean?.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }else{
            apiService?.getDistInstitutionSpinnerIp(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,facility_id!!, userDataStoreBean?.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }

    fun getDistrictTestName(toggle: String,institutionId :  MutableList<Int>, ResponseTestAssignedToRetrofitCallback: RetrofitCallback<LabFilterResponseModel>) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {
            val institution = JSONArray()
            for(item in institutionId){
                institution.put(item)
            }
            jsonBody.put("user_Id", userDataStoreBean?.uuid!!)
            jsonBody.put("institution_Id", institution)


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

        if(toggle.equals("OP")){
            apiService?.getDistTestNameSpinner(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,facility_id!!, userDataStoreBean?.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }else{
            apiService?.getDistTestNameSpinnerIp(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,facility_id!!, userDataStoreBean?.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }

    fun getDistrictLabName(toggle: String,institutionId :  MutableList<Int>, ResponseTestAssignedToRetrofitCallback: RetrofitCallback<LabFilterResponseModel>) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {
            val institution = JSONArray()
            for(item in institutionId){
                institution.put(item)
            }
            jsonBody.put("user_Id", userDataStoreBean?.uuid!!)
            jsonBody.put("institution_Id", institution)

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

        if(toggle.equals("OP")){
            apiService?.getDistLabNameSpinner(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,
                facility_id!!, userDataStoreBean?.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }else{
            apiService?.getDistLabNameSpinnerIp(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,
                facility_id!!, userDataStoreBean?.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }

    fun getDistrictGender(toggle: String, ResponseTestAssignedToRetrofitCallback: RetrofitCallback<LabFilterResponseModel>) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {

            jsonBody.put("user_Id", userDataStoreBean?.uuid!!)

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

        if(toggle.equals("OP")){
            apiService?.getDistGenderSpinner(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,
                facility_id!!, userDataStoreBean?.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }else{
            apiService?.getDistGenderSpinnerIp(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,
                facility_id!!, userDataStoreBean?.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }
    fun getDistrictStatus(toggle: String, ResponseTestAssignedToRetrofitCallback: RetrofitCallback<LabFilterResponseModel>) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {

            jsonBody.put("user_Id", userDataStoreBean?.uuid!!)

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

        if(toggle.equals("OP")){
            apiService?.getDistStatusSpinner(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,
                facility_id!!, userDataStoreBean?.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }else{
            apiService?.getDistStatusSpinnerIp(AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,
                facility_id!!, userDataStoreBean?.user_name,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

        }

    }
}