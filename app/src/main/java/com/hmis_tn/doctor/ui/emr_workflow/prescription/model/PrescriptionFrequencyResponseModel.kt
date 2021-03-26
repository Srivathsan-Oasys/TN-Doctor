package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

data class PrescriptionFrequencyResponseModel(
    val responseContents: List<PrescriptionFrequencyresponseContent?>? = listOf(),
    val message: String? = "",
    val req: ReqX? = ReqX(),
    val status: Int? = 0,
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)