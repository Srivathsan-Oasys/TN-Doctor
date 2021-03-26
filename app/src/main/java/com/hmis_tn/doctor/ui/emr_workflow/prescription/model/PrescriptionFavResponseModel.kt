package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

data class PrescriptionFavResponseModel(
    val code: Int = 0,
    val message: String = "",
    val responseContentLength: Int = 0,
    val responseContents: List<PrescriptionFavResponseContent> = listOf()
)