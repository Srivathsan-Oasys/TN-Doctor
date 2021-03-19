package com.oasys.digihealth.doctor.ui.emr_workflow.history.diagnosis.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HistoryDiagnosisViewModelFactory(
    private var application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryDiagnosisViewModel(
            application
        ) as T
    }
}
