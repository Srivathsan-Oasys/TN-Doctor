package com.hmis_tn.doctor.ui.emr_workflow.certificate.model

data class TemplateItemResponseModel(
    val req: String = "",
    val responseContents: TemplateItemResponseContents = TemplateItemResponseContents(),
    val statusCode: Int = 0
)