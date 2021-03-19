package com.oasys.digihealth.doctor.ui.login.view_model

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.application.HmisApplication
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.ui.institution.common_departmant.model.DepartmentResponseModel
import com.oasys.digihealth.doctor.ui.institution.lmis.model.LocationMasterResponseModel
import com.oasys.digihealth.doctor.ui.login.model.ChangePasswordOTPResponseModel
import com.oasys.digihealth.doctor.ui.login.model.LoginSessionRequest
import com.oasys.digihealth.doctor.ui.login.model.PasswordChangeResponseModel
import com.oasys.digihealth.doctor.ui.login.model.SimpleResponseModel
import com.oasys.digihealth.doctor.ui.login.model.institution_response.InstitutionResponseModel
import com.oasys.digihealth.doctor.ui.login.model.login_response_model.LoginResponseContents
import com.oasys.digihealth.doctor.ui.login.model.login_response_model.LoginResponseModel
import com.oasys.digihealth.doctor.ui.login.model.office_response.OfficeResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class LoginViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {
    var username = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()

    var loginLayout = MutableLiveData<Int>()
    var sendOptLayout = MutableLiveData<Int>()
    var forgetUsernemeLayout = MutableLiveData<Int>()
    var changePasswordLayout = MutableLiveData<Int>()

    var forgotpasswordusername = MutableLiveData<String>()
    var otp = MutableLiveData<String>()
    var changePassword = MutableLiveData<String>()
    var confirmPassword = MutableLiveData<String>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    lateinit var loginRetrofitCallBack: RetrofitCallback<LoginResponseModel?>
    lateinit var otpRetrofitCallBack: RetrofitCallback<ChangePasswordOTPResponseModel?>
    lateinit var changePasswordRetrofitCallBack: RetrofitCallback<PasswordChangeResponseModel?>


    init {
        username.value = "parthidoc"//todo
        password.value = "123456"
        forgotpasswordusername.value = ""
        otp.value = ""
        changePassword.value = ""
        confirmPassword.value = ""
        loginLayout.value = 0
        sendOptLayout.value = 8
        forgetUsernemeLayout.value = 0
        changePasswordLayout.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
        //  utils=Utils(this)
    }

    fun onLoginClicked(passwordEncryptValue: Any) {
        if (!Utils.isNetworkConnected(getApplication())) {
            Toast.makeText(
                getApplication(),
                getApplication<Application>().getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
//            Toast.makeText(getApplication(),getApplication<Application>().getString(R.string.no_internet),Toast.LENGTH_SHORT).show()
            return
        }
        if (username.value == "") {
            errorText.value = "Please Enter username"
            return
        }
        if (password.value == "") {
            errorText.value = "Please Enter password"
            return
        }
        progress.value = 0

        val jsonBody = JSONObject()
        try {
            jsonBody.put("username", username.value!!.toString().trim())
            jsonBody.put("password", passwordEncryptValue.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.getLoginDetails(username.value, passwordEncryptValue.toString())?.enqueue(
            RetrofitMainCallback(loginRetrofitCallBack)
        )

//        apiService?.getLoginDetails(body)?.enqueue(RetrofitMainCallback(loginRetrofitCallBack))
        return
    }

    fun visisbleSendOTp() {
        loginLayout.value = 8
        sendOptLayout.value = 0
    }

    fun visisbleLogin() {
        loginLayout.value = 0
        sendOptLayout.value = 8
        forgetUsernemeLayout.value = 0
        changePasswordLayout.value = 8
        forgotpasswordusername.value = ""
        otp.value = ""
        changePassword.value = ""
        confirmPassword.value = ""
    }

    fun validateSendOTp() {
        if (!Utils.isNetworkConnected(getApplication())) {
            Toast.makeText(
                getApplication(),
                getApplication<Application>().getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (forgotpasswordusername.value!!.trim().isEmpty()) {
            errorText.value = "Please Enter username/Mobile number"
            return
        }
        // api call for send otp
        val jsonBody = JSONObject()
        try {
            jsonBody.put("username", forgotpasswordusername.value!!.trim())
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
        apiService?.getOtpForPasswordChange(body)!!
            .enqueue(RetrofitMainCallback(otpRetrofitCallBack))
        //responce success visiblity change
        return
    }

    fun validateChangePassword() {
        if (!Utils.isNetworkConnected(getApplication())) {
            Toast.makeText(
                getApplication(),
                getApplication<Application>().getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (otp.value!!.trim().isEmpty()) {
            errorText.value = "Please Enter Otp"
            return
        }
        if (changePassword.value!!.trim().isEmpty()) {
            errorText.value = "Please Enter Password"
            return
        }
        if (confirmPassword.value!!.trim().isEmpty()) {
            errorText.value = "Please Enter Confirm Password"
            return
        }
        if (changePassword.value!!.trim() != confirmPassword.value!!.trim()) {
            errorText.value = "Please Check Change Password & Confirm Password Mismatched"
            return
        }
        // api call for change password & call login view again
        val jsonBody = JSONObject()
        try {
            jsonBody.put("username", forgotpasswordusername.value!!.trim())
            jsonBody.put("otp", otp.value!!.trim())
            jsonBody.put("password", Utils.encrypt(changePassword.value!!.trim()).toString())
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
        apiService?.getPasswordChanged(body)
            ?.enqueue(RetrofitMainCallback(changePasswordRetrofitCallBack))

        return
    }


    fun getfacilityCallback(
        userId: Int?,
        facilityCallback: RetrofitCallback<InstitutionResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            Toast.makeText(
                getApplication(),
                getApplication<Application>().getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("userId", userId.toString())
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
        apiService?.getFaciltyList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userId!!, body
        )?.enqueue(RetrofitMainCallback(facilityCallback))
    }

    fun getDepartmentList(
        facilitylevelID: Int?,
        facilityUserID: Int?,
        depatmentCallback: RetrofitCallback<DepartmentResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            Toast.makeText(
                getApplication(),
                getApplication<Application>().getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("facility_uuid", facilitylevelID)
            jsonBody.put("Id", facilityUserID)
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

        apiService?.getDepartmentList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            body
        )?.enqueue(RetrofitMainCallback(depatmentCallback))
        return
    }


    fun getLocationMaster(
        facility: Int,
        stateRetrofitCallback: RetrofitCallback<LocationMasterResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            Toast.makeText(
                getApplication(),
                getApplication<Application>().getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("facility_uuid", facility)
            jsonBody.put("paginationSize", 1000)
            //      jsonBody.put("department_uuid", department)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )


        // request.department_uuid=department

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getLocationMasterLogin(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility,
            body
        )?.enqueue(RetrofitMainCallback(stateRetrofitCallback))

        return
    }


    fun getOfficeList(officeRetrofitCallBack: RetrofitCallback<OfficeResponseModel>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            Toast.makeText(
                getApplication(),
                getApplication<Application>().getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("user_uuid", userDataStoreBean?.uuid!!)
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

        println("AppConstantsBEARER_AUTqewfregtry5= ${AppConstants.BEARER_AUTH + userDataStoreBean?.access_token}")

        apiService?.getOfficeList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            body
        )?.enqueue(RetrofitMainCallback(officeRetrofitCallBack))
        return
    }


    fun Loginseassion(
        responseContents: LoginResponseContents,
        sessionRequest: LoginSessionRequest,
        loginSeasionRetrofitCallBack: RetrofitCallback<SimpleResponseModel>
    ) {

        val session = sessionRequest

        if (!Utils.isNetworkConnected(getApplication())) {
            Toast.makeText(
                getApplication(),
                getApplication<Application>().getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        progress.value = 0

        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.LoginSession(
            AppConstants.BEARER_AUTH + responseContents.userDetails?.access_token,
            session.LoginId!!,
            session.Password,
            sessionRequest
        )?.enqueue(RetrofitMainCallback(loginSeasionRetrofitCallBack))
        return
    }


}