package com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider

class AddSurgeryViewModelFactory(
    private var application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddSurgeryViewModel(
            application
        ) as T
    }
}
