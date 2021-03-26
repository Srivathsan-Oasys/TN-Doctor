package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response

data class TreatmentKitDiagnosi(
    val created_by: String? = "",
    val created_date: String? = "",
    val diagnosis_uuid: Int? = 0,
    val is_active: Int? = 0,
    val modified_by: String? = "",
    val modified_date: String? = "",
    val revision: Int? = 0,
    val status: Int? = 0,
    val treatment_kit_uuid: Int? = 0,
    val user_uuid: String? = ""
)