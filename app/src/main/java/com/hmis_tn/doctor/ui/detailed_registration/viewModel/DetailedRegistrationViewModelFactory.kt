package com.hmis_tn.doctor.ui.detailedRegistration.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DetailedRegistrationViewModelFactory(
    private var application: Application?
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailedRegistrationViewModel(
            application
        ) as T
    }
}
