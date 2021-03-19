package com.oasys.digihealth.doctor.ui.quick_reg.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LabTestWiseReportViewModelFactory (
    private  var application: Application?) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LabTestWiseReportViewModel(
            application
        ) as T
    }
}