package com.oasys.digihealth.doctor.ui.home

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.application.HmisApplication
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitMainCallback
import com.oasys.digihealth.doctor.ui.login.model.SimpleResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    fun LogOutseassion(
        FacilityId: Int,
        loginSeasionRetrofitCallBack: RetrofitCallback<SimpleResponseModel>
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
            jsonBody.put("session_id", userDataStoreBean?.SessionId)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.LogoutSeasion(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            FacilityId,
            userDataStoreBean.SessionId,
            body
        )?.enqueue(RetrofitMainCallback(loginSeasionRetrofitCallBack))
        return


    }

    var errorText = MutableLiveData<String>()
    var progressBar = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    init {
        progressBar.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
    }
}