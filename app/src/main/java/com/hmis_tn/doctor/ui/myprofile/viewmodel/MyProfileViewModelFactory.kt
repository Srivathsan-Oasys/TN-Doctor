package com.hmis_tn.doctor.ui.login.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hmis_tn.doctor.ui.myprofile.viewmodel.MyProfileViewModel

class MyProfileViewModelFactory (
    private  var application: Application?) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyProfileViewModel(
            application

        ) as T
    }
}
