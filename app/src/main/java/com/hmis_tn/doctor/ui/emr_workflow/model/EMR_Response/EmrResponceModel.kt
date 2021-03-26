package com.hmis_tn.doctor.ui.emr_workflow.model.EMR_Response

data class EmrResponceModel(
    val message: String = "",
    val responseContents: List<ResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)