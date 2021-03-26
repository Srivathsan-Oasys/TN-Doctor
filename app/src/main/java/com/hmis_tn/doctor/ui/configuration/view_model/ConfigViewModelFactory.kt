package com.hmis_tn.doctor.ui.configuration.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ConfigViewModelFactory(
    private var application: Application?
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ConfigViewModel(
            application

        ) as T
    }
}
