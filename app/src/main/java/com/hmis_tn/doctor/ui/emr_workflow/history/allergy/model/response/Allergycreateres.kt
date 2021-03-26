package com.hmis_tn.doctor.ui.emr_workflow.history.allergy.model.response

data class Allergycreateres(
    val allergy_master_uuid: String? = "",
    val allergy_severity_uuid: String? = "",
    val allergy_source_uuid: String? = "",
    val allergy_type_uuid: String? = "",
    val consultation_uuid: Int? = 0,
    val created_by: String? = "",
    val created_date: String? = "",
    val duration: String? = "",
    val encounter_uuid: Int? = 0,
    val end_date: String? = "",
    val is_active: Int? = 0,
    val modified_by: String? = "",
    val modified_date: String? = "",
    val patient_allergy_status_uuid: Int? = 0,
    val patient_uuid: String? = "",
    val performed_by: String? = "",
    val performed_date: String? = "",
    val _uuid: String? = "",
    val revision: Int? = 0,
    val start_date: String? = "",
    val status: Int? = 0,
    val user_uuid: String? = ""
)