package com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.model


data class DiagnosisListResponseModel(
    val responseContents: List<DiagnosisResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0
)