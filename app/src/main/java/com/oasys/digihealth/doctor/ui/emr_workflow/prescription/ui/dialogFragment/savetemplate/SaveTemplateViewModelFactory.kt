package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class SaveTemplateViewModelFactory(
    private var application: Application?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SaveTemplateViewModel(
            application
        ) as T
    }
}