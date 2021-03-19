package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model

data class InvestigationPostResponseModel(
    val message: String = "",
    val responseContents: List<InvestigationPostResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)