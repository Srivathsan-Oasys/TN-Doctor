package com.hmis_tn.doctor.ui.emr_workflow.view.lab.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.InvestigationPrevResponseModel

class PreviewInvestigationViewModelFactory(
    private val mApplication: Application,
    private val prevLabrecordsRetrofitCallback: RetrofitCallback<InvestigationPrevResponseModel>


) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PreviewInvestigationViewModel(mApplication, prevLabrecordsRetrofitCallback) as T
    }
}