package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model

data class PrevRadiologyResponseModel(
    val message: String = "",
    val responseContents: List<PrevsRadiologyResponseContent> = listOf(),
    val statusCode: Int = 0
)