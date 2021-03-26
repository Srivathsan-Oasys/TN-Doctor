package com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DiagonosisDialogViewModelFactory(
    private var application: Application?
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DiagonosisDialogViewModel(
            application
        ) as T
    }
}