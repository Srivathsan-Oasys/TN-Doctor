package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model

data class PrescriptionRoutResponseModel(
    val message: String = "",
    val responseContents: List<PrescriptionRouteResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)