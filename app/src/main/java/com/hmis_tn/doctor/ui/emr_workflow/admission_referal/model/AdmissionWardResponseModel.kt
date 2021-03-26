package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model

data class AdmissionWardResponseModel(
    val msg: String = "",
    val responseContents: List<AmissionWardResponseContent> = listOf(),
    val status: String = "",
    val statusCode: Int = 0
)