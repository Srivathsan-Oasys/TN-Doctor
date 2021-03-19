package com.oasys.digihealth.doctor.ui.login.setpassword.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.application.HmisApplication
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.ui.login.model.SimpleResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class SetPasswordViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {
    var enterNewPasswordEditText = MutableLiveData<String>()
    var enterConfirmPasswordEditText = MutableLiveData<String>()

    var errorText = MutableLiveData<String>()

    var progressBar = MutableLiveData<Int>()

    var userDetailsRoomRepository: UserDetailsRoomRepository? = null


    init {
        enterNewPasswordEditText.value = ""
        enterConfirmPasswordEditText.value = ""
        progressBar.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
    }

    fun onChangePassword(
        userName: String,
        passwordEncryptValue: String,
        changePasswordCallback: RetrofitCallback<SimpleResponseModel?>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
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
            jsonBody.put("password", passwordEncryptValue)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )


        Log.i("req", jsonBody.toString())
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        //val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.setPassword(body)?.enqueue(RetrofitMainCallback(changePasswordCallback))
        return
    }


}
