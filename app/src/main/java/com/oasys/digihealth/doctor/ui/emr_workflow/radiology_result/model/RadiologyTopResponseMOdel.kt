package com.oasys.digihealth.doctor.ui.emr_workflow.radiology_result.model

data class RadiologyTopResponseMOdel(
    val message: String = "",
    val responseContents: List<RadiologyTopResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)