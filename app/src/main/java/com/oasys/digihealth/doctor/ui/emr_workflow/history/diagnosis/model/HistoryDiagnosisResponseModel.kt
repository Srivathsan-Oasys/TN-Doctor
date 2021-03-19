package com.oasys.digihealth.doctor.ui.emr_workflow.history.diagnosis.model

data class HistoryDiagnosisResponseModel(
    val responseContents: List<HistoryDiagnosisresponseContent?>? = listOf(),
    val code: Int? = 0,
    val message: String? = ""
)