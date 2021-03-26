package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.response

data class AdmissionPatientRefResponseModel(
    val responseContents: List<AdmissionPatientRefresponseContent>? = listOf(),
    val code: Int? = 0,
    val message: String? = ""
)