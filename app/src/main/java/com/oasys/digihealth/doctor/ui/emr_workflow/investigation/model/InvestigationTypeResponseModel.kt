package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model

data class InvestigationTypeResponseModel(
    val req: String = "",
    val responseContents: List<InvestigationTypeResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)