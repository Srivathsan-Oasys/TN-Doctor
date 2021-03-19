package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model

data class InvestigationSearchResponseModel(
    val message: String = "",
    val responseContents: List<InvestigationSearchResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)