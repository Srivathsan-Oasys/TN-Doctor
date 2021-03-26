package com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hmis_tn.doctor.ui.emr_workflow.investigation_result.view_model.InvestigationResultViewModel

class InvestigationResultViewModelFactory(private var application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InvestigationResultViewModel(
            application
        ) as T
    }
}
