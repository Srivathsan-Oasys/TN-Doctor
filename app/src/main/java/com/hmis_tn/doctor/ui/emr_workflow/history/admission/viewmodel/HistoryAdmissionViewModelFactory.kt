package com.hmis_tn.doctor.ui.emr_workflow.history.admission.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider

class HistoryAdmissionViewModelFactory(
    private var application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryAdmissionViewModel(
            application
        ) as T
    }
}
