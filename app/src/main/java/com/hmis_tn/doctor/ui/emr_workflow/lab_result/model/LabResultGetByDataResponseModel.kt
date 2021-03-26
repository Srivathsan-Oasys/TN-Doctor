package com.hmis_tn.doctor.ui.emr_workflow.lab_result.model

data class LabResultGetByDataResponseModel(
    val message: String = "",
    val responseContents: List<LabResultGetByDataResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)