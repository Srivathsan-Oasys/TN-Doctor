package com.hmis_tn.doctor.ui.emr_workflow.history.lab.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider

class HistoryLabViewModelFactory(
    private var application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryLabViewModel(
            application
        ) as T
    }
}
