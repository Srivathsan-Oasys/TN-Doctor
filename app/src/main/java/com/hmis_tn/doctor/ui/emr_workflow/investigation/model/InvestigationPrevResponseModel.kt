package com.hmis_tn.doctor.ui.emr_workflow.investigation.model

data class InvestigationPrevResponseModel(
    val message: String = "",
    val responseContents: List<InvestigationPrevResponseContent> = listOf(),
    val statusCode: Int = 0
)