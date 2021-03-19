package com.oasys.digihealth.doctor.ui.helpdesk.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserTicketViewModelFactory(
    private var application: Application?
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserTicketViewModel(
            application
        ) as T
    }
}
