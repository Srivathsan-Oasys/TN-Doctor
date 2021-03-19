package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PrescriptionCommentViewModelFactory(
    private var application: Application?
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PrescriptionCommentViewModel(
            application
        ) as T
    }
}
