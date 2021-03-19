package com.oasys.digihealth.doctor.ui.emr_workflow.history.familyhistory.viewmodel


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HistoryFamilyViewModelFactory(
    private var application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryFamilyViewModel(
            application
        ) as T
    }
}
