package com.oasys.digihealth.doctor.ui.tutorial.view

import com.oasys.digihealth.doctor.ui.tutorial.model.UserModuleResponseContent

interface TutorialCallback {

    fun OnDeleteClick(model: UserModuleResponseContent?)
    fun OnDownloadClick(model: UserModuleResponseContent?)
}