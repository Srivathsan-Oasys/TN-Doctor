package com.hmis_tn.doctor.ui.emr_workflow.history.radiology.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider

class HistoryRadiologyViewModelFactory(
    private var application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryRadiologyViewModel(
            application
        ) as T
    }
}
