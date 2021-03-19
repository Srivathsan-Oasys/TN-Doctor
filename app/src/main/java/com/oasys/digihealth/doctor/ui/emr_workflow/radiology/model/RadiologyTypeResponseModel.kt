package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model

data class RadiologyTypeResponseModel(
    val req: String = "",
    val responseContents: List<RadiologyTypeResponseContent?>? = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)