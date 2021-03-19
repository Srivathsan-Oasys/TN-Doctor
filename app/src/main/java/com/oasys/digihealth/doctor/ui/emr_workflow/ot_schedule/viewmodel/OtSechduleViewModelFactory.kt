package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider

class OtSechduleViewModelFactory(
    private var application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OtScheduleViewModel(
            application
        ) as T
    }
}
