package com.hmis_tn.doctor.ui.emr_workflow.investigation_result.model

data class InvestigationResultResponseModel(
    val message: String = "",
    val responseContents: List<InvestigationResultResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)