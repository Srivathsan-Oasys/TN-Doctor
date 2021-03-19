package com.oasys.digihealth.doctor.ui.emr_workflow.view.lab.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.view.lab.model.PrevLabResponseModel

class PreviewViewModelFactory(
    private val mApplication: Application,
    private val prevLabrecordsRetrofitCallback: RetrofitCallback<PrevLabResponseModel>


) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PreviewViewModel(mApplication, prevLabrecordsRetrofitCallback) as T
    }
}