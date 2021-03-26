package com.hmis_tn.doctor.ui.emr_workflow.mrd.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MRDViewModelFactory(
    private var application: Application


) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MRDViewModel(
            application
        ) as T
    }
}
