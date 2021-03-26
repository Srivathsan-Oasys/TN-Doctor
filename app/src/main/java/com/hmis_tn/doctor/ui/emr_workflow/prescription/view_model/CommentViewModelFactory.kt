package com.hmis_tn.doctor.ui.emr_workflow.prescription.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CommentViewModelFactory(
    private val mApplication: Application


) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommentViewModel(mApplication) as T
    }
}