package com.oasys.digihealth.doctor.ui.login.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.myprofile.model.MyProfileResponseModel
import com.oasys.digihealth.doctor.ui.myprofile.viewmodel.MyProfileViewModel

class MyProfileViewModelFactory (
    private  var application: Application?) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyProfileViewModel(
            application

        ) as T
    }
}
