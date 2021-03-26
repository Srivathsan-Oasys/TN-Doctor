package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model

data class AdmissionDepartmentResponsemodel(
    val responseContent: AdmissionResponseContent = AdmissionResponseContent(),
    val status: String = "",
    val statusCode: Int = 0
)