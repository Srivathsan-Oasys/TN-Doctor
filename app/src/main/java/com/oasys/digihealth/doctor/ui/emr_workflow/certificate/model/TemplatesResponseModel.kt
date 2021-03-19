package com.oasys.digihealth.doctor.ui.emr_workflow.certificate.model

data class TemplatesResponseModel(
    val message: String = "",
    val req: String = "",
    val responseContents: List<TemplatesResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)