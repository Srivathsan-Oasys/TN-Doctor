package com.hmis_tn.doctor.ui.quickregistration.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class QuickRegistrationNewViewModelFactory (
    private  var application: Application?) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QuickRegistrationNewViewModel(
            application
        ) as T
    }
}
