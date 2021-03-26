package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model

data class TransmissionReasonResponseContent(
    val code: String = "",
    val is_active: Boolean = false,
    val name: String = "",
    val status: Boolean = false,
    val uuid: Int = 0
)