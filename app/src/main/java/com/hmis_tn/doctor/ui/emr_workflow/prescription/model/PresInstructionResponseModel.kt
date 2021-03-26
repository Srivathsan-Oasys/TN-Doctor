package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

data class PresInstructionResponseModel(
    val message: String = "",
    val responseContents: List<PresInstructionResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)