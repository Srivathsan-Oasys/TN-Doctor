package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model

data class ManageRadiologyTemplateResponseModel(
    val code: Int = 0,
    val message: String = "",
    val responseContent: ManageRadiologyTemplateResponseContent = ManageRadiologyTemplateResponseContent()
)