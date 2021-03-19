package com.oasys.digihealth.doctor.ui.emr_workflow.lab.view_model


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ManageLabPervLabViewModelFactory(
    private var application: Application?
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ManageLabPrevLabViewModel(
            application
        ) as T
    }
}
