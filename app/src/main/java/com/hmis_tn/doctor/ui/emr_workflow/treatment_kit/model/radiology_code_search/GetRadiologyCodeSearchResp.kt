package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.radiology_code_search

data class GetRadiologyCodeSearchResp(
    val message: String? = "",
    val responseContents: List<ResponseContent>? = listOf(),
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)