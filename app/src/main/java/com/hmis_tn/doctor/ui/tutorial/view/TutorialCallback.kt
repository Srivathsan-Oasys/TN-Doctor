package com.hmis_tn.doctor.ui.tutorial.view

import com.hmis_tn.doctor.ui.tutorial.model.UserModuleResponseContent

interface TutorialCallback {

    fun OnDeleteClick(model: UserModuleResponseContent?)
    fun OnDownloadClick(model: UserModuleResponseContent?)
}