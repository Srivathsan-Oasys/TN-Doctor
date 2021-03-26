package com.hmis_tn.doctor.ui.helpdesk.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EditTicketViewModelFactory(
    private var application: Application?
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditTicketViewModel(
            application
        ) as T
    }
}
