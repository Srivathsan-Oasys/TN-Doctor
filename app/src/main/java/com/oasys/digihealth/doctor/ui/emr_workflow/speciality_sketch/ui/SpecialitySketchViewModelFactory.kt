package com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.viewmodel.SpecialitySketchViewModel

class SpecialitySketchViewModelFactory(
    private var application: Application


) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SpecialitySketchViewModel(
            application
        ) as T
    }
}