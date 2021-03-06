package com.hmis_tn.doctor.ui.emr_workflow.prescription.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PrescriptionPreviewModelFactory(
    private val mApplication: Application


) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PrescriptionPreviewModel(mApplication) as T
    }
}