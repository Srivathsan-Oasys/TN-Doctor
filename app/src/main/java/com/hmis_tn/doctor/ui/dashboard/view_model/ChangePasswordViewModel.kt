package com.hmis_tn.doctor.ui.dashboard.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitMainCallback
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject


class ChangePasswordViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {
    var enterOTPEditText = MutableLiveData<String>()
    var enterNewPasswordEditText = MutableLiveData<String>()
    var enterConfirmPasswordEditText = MutableLiveData<String>()

    var errorText = MutableLiveData<String>()

    var progressBar = MutableLiveData<Int>()

    var userDetailsRoomRepository: UserDetailsRoomRepository? = null


    init {
        enterOTPEditText.value = ""
        enterNewPasswordEditText.value = ""
        enterConfirmPasswordEditText.value = ""
        progressBar.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
    }

    fun getOtp(
        userName: String,
        facility_uuid: Int,
        otpRetrofitCallBack: RetrofitCallback<com.hmis_tn.doctor.ui.login.model.ChangePasswordOTPResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("username", userName)
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
        apiService?.getOtpForPasswordChange(body)!!.enqueue(
            RetrofitMainCallback(otpRetrofitCallBack)
        )
    }


    fun onChangePassword(
        userName: String,
        otp: String,
        passwordEncryptValue: String,
        changePasswordCallback: RetrofitCallback<com.hmis_tn.doctor.ui.login.model.PasswordChangeResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        if (enterOTPEditText.value!!.trim().isEmpty()) {
            errorText.value = "Please Enter OTP"
            return
        }
        if (enterNewPasswordEditText.value!!.trim().isEmpty()) {
            errorText.value = "Please Enter New Password"
            return
        }

        if (enterConfirmPasswordEditText.value!!.trim().isEmpty()) {
            errorText.value = "Please Enter Confirm Password"
            return
        }

        if (enterNewPasswordEditText.value != enterConfirmPasswordEditText.value) {
            errorText.value = "Password Mismatch"
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("username", userName)
            jsonBody.put("otp", otp)
            jsonBody.put("password", passwordEncryptValue)
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
        //val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getPasswordChanged(body)?.enqueue(RetrofitMainCallback(changePasswordCallback))
        return
    }

}