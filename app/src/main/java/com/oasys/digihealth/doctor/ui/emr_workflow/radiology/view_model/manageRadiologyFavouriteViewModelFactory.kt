package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ManageRadiologyFavourteViewModelFactory(
    private var application: Application?
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ManageRadiologyFavourteViewModel(
            application
        ) as T
    }
}