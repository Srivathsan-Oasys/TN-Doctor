package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ManageInvestFavourteViewModelFactory(
    private var application: Application?
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ManageInvestFavourteViewModel(
            application
        ) as T
    }
}