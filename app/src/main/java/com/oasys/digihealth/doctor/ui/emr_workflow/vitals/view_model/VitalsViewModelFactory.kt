package com.oasys.digihealth.doctor.ui.emr_workflow.vitals.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VitalsViewModelFactory(
    private var application: Application


) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VitalsViewModel(
            application
        ) as T
    }
}
