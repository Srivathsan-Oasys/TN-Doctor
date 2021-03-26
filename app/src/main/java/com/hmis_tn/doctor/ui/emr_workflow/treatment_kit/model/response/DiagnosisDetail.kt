package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response

data class DiagnosisDetail(
    val diagnosis_code: String? = "",
    val diagnosis_description: String? = "",
    val diagnosis_id: Int? = 0,
    val diagnosis_name: String? = ""
)