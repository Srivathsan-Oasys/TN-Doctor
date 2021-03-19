package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model

data class ManageRadiologyFavAddResponseModel(
    val code: Int = 0,
    val message: String = "",
    val responseContentLength: Int = 0,
    val responseContents: ManageRadiologyFavAddResponseContents = ManageRadiologyFavAddResponseContents()
)