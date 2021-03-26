package com.hmis_tn.doctor.ui.emr_workflow.history.allergy.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AllergyViewModelFactory(
    private var application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllergyViewModel(
            application
        ) as T
    }
}
