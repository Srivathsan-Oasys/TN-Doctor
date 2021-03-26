package com.hmis_tn.doctor.ui.institute.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.login.model.office_response.OfficeResponseModel

class InstituteViewModelFactory(
    private var application: Application?,
    private var officeRetrofitCallBack: RetrofitCallback<OfficeResponseModel>
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InstituteViewModel(
            application,
            officeRetrofitCallBack
        ) as T
    }
}
