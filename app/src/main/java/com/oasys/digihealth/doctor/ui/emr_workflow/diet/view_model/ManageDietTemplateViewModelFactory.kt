package com.oasys.digihealth.doctor.ui.emr_workflow.diet.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ManageDietTemplateViewModelFactory(
    private var application: Application


) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ManageDietTemplateViewModel(
            application
        ) as T
    }
}