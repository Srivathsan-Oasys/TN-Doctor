package com.hmis_tn.doctor.ui.emr_workflow.diagnosis.model

data class PreDiagnosisResponseModel(
    val code: Int = 0,
    val message: String = "",
    val responseContents: List<PreDiagnosisResponseContent> = listOf()
)