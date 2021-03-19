package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.dialogFragment.templeteEdit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.dialogFragment.favouriteEdit.ManagePreFAvAddEditviewModel

class ManagePreFavAddEditviewModelFactory(
    private val mApplication: Application


) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ManagePreFAvAddEditviewModel(mApplication) as T
    }
}