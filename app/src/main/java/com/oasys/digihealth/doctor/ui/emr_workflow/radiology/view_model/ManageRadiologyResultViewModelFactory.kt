package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ManageRadiologyResultViewModelFactory(
    private var application: Application


) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ManageRadiologyResultViewModel(
            application
        ) as T
    }
}
