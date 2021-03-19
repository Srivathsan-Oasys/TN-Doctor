package com.oasys.digihealth.doctor.ui.emr_workflow.certificate.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CertificateViewModelFactory(
    private var application: Application


) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CertificateViewModel(
            application
        ) as T
    }
}
