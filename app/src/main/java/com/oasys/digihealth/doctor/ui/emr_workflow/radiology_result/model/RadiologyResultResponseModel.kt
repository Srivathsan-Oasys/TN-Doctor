package com.oasys.digihealth.doctor.ui.emr_workflow.radiology_result.model

data class RadiologyResultResponseModel(
    val message: String = "",
    val responseContents: List<RadilogyResultResponseContent> = listOf(),
    val statusCode: Int = 0
)