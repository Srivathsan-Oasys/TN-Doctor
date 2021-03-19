package com.oasys.digihealth.doctor.ui.emr_workflow.history.config.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HistoryConfigViewModelFactory(
    private var application: Application?
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryConfigViewModel(
            application

        ) as T
    }
}
