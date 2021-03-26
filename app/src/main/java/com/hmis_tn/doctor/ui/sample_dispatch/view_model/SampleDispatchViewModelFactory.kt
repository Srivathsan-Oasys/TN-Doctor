package com.hmis_tn.doctor.ui.sample_dispatch.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SampleDispatchViewModelFactory(
    private var application: Application?
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SampleDispatchViewModel(
            application
        ) as T
    }
}
