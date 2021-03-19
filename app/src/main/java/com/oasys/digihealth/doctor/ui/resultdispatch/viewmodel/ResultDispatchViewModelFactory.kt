package com.oasys.digihealth.doctor.ui.resultdispatch.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ResultDispatchViewModelFactory(
    private var application: Application?
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ResultDispatchViewModel(
            application
        ) as T
    }
}
