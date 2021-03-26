package com.hmis_tn.doctor.ui.emr_workflow.history.immunization.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider

class HistoryImmunizationViewModelFactory(
    private var application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryImmunizationViewModel(
            application
        ) as T
    }
}
