package com.oasys.digihealth.doctor.ui.emr_workflow.history.surgery.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider

class HistorySurgeryViewModelFactory(
    private var application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistorySurgeryViewModel(
            application
        ) as T
    }
}
