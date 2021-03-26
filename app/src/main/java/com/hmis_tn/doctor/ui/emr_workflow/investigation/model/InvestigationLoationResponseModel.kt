package com.hmis_tn.doctor.ui.emr_workflow.investigation.model

data class InvestigationLoationResponseModel(
    val message: String = "",
    val responseContents: List<InvestigationLocationResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)