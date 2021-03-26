package com.hmis_tn.doctor.ui.order_status.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class OrderStatusViewModelFactory(
    private var application: Application?
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OrderStatusViewModel(
            application
        ) as T
    }
}
