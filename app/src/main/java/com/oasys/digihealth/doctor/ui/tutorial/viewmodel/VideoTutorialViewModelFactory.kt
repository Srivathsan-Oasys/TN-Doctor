package com.oasys.digihealth.doctor.ui.tutorial.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VideoTutorialViewModelFactory(
    private var application: Application?
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VideoTutorialViewModel(
            application
        ) as T
    }
}
