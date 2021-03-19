package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_medication.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DischargeMedicationViewModelFactory(private var application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DischargeMedicationViewModel(
            application
        ) as T
    }
}