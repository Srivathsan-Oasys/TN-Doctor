package com.oasys.digihealth.doctor.ui.emr_workflow.history.diagnosis.model

data class CreateDiagnosisRequestModel(
    val body_site_uuid: Int? = 0,
    val category_uuid: Int? = 0,
    val comments: String? = "",
    val condition_status_uuid: Int? = 0,
    val condition_type_uuid: Int? = 0,
    val consultation_uuid: Int? = 0,
    val department_uuid: String? = "",
    val diagnosis_uuid: Int? = 0,
    val encounter_type_uuid: String? = "",
    val encounter_uuid: Int? = 0,
    val facility_uuid: String? = "",
    val grade_uuid: Int? = 0,
    val patient_treatment_uuid: Int? = 0,
    val patient_uuid: String? = "",
    val prescription_uuid: Int? = 0,
    val tat_end_time: String? = "",
    val tat_start_time: String? = "",
    val type_uuid: Int? = 0
)