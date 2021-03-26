package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model

data class TransmissionReasonResponseModel(
    val code: Int = 0,
    val message: String = "",
    val responseContent: List<TransmissionReasonResponseContent> = listOf()
)