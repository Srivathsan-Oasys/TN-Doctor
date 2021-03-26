package com.hmis_tn.doctor.ui.emr_workflow.history.vitals.model.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HistoryVitalsViewModelFactory(
    private var application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryVitalsViewModel(
            application
        ) as T
    }
}
