package com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.model

data class DiagnosisResponseContent(
    val code: String? = "",
    val name: String? = "",
    val first_name: String? = "",
    val uuid: Int? = 0,
    val description: String? = "",
    val diagnosis_scheme_uuid: Int? = 0,
    val diagnosis_type_uuid: Int? = 0,
    val diagnosis_category_uuid: Int? = 0,
    val diagnosis_grade_uuid: Int? = 0,
    val diagnosis_region_uuid: Int? = 0,
    val diagnosis_version_uuid: Int? = 0,
    val facility_uuid: Int? = 0,
    val department_uuid: Int? = 0,
    val is_active: Boolean? = false,
    val status: Boolean? = false,
    val revision: Int? = 0
)