package com.hmis_tn.doctor.ui.emr_workflow.diet.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ManageDietFavouriteViewModelFactory(
    private var application: Application


) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ManageDietFavouriteViewModel(
            application
        ) as T
    }
}