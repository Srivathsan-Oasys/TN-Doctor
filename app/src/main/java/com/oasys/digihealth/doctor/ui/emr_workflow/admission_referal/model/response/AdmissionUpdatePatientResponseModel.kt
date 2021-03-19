package com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.response

data class AdmissionUpdatePatientResponseModel(
    val code: Int? = 0,
    val message: String? = "",
    val responseContent: List<Int>? = listOf(),
    val status: String? = ""
)