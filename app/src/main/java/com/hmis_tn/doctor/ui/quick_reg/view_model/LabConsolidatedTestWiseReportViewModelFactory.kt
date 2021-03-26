package com.hmis_tn.doctor.ui.quick_reg.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LabConsolidatedTestWiseReportViewModelFactory(
    private var application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LabConsolidatedTestWiseReportViewModel(
            application
        ) as T
    }
}