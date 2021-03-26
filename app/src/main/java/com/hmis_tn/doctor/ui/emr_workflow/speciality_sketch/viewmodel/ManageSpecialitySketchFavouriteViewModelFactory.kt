package com.hmis_tn.doctor.ui.emr_workflow.speciality_sketch.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ManageSpecialitySketchFavouriteViewModelFactory(
    private var application: Application


) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ManageSpecialitySketchFavouriteViewModel(
            application
        ) as T
    }
}