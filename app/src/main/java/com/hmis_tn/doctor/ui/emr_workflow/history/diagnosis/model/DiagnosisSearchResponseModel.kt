package com.hmis_tn.doctor.ui.emr_workflow.history.diagnosis.model

data class DiagnosisSearchResponseModel(
    val responseContents: List<DiagresponseContent?>? = listOf(),
    val code: Int? = 0,
    val message: String? = ""
)