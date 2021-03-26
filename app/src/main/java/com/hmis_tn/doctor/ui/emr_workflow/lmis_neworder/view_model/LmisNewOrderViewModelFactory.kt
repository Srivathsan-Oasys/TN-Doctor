package com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LmisNewOrderViewModelFactory(
    private var application: Application


) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LmisNewOrderViewModel(
            application
        ) as T
    }
}
