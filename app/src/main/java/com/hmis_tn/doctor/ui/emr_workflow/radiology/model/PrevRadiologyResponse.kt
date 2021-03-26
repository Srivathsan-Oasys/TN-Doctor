package com.hmis_tn.doctor.ui.emr_workflow.radiology.model

data class PrevRadiologyResponse(
    val message: String = "",
    val responseContents: List<PrevRadiologyResponseContent> = listOf(),
    val statusCode: Int = 0
)