package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.investigation_code_search

data class GetInvestigationCodeSearchResp(
    val message: String? = "",
    val responseContents: List<ResponseContent>? = listOf(),
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)