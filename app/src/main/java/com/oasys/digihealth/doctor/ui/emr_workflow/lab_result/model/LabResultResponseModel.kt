package com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.model

data class LabResultResponseModel(
    val message: String = "",
    val responseContents: List<LabResultResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)