package com.oasys.digihealth.doctor.ui.emr_workflow.history.admission.model

data class AdmissionReferalResponseModel(
    val responseContent: List<AdmissionReferalresponseContent?>? = listOf(),
    val code: Int? = 0,
    val message: String? = ""
)