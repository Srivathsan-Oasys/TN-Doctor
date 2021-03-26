package com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SnomedDialogviewModelFactory(
    private var application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SnomedDialogviewModel(
            application
        ) as T
    }
}
