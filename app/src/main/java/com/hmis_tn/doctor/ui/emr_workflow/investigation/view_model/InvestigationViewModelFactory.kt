package com.hmis_tn.doctor.ui.emr_workflow.investigation.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class InvestigationViewModelFactory(
    private var application: Application


) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InvestigationViewModel(
            application
        ) as T
    }
}
