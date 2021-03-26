package com.hmis_tn.doctor.ui.out_patient.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DischargePatientViewModelFactory(
    private var application: Application?
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DischargePatientViewModel(
            application
        ) as T
    }
}