package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model

data class PatientTKCreatedData(
    val created_by: Int? = 0,
    val created_: String? = "",
    val department_uuid: String? = "",
    val encounter_type_uuid: Int? = 0,
    val encounter_uuid: Int? = 0,
    val facility_uuid: String? = "",
    val is_active: Boolean? = false,
    val modified_by: Int? = 0,
    val modified_: String? = "",
    val patient_uuid: String? = "",
    val revision: Int? = 0,
    val status: Boolean? = false,
    val tat_start_time: String? = "",
    val treatment_given_by: String? = "",
    val treatment_given_: String? = "",
    val treatment_kit_uuid: Int? = 0,
    val treatment_status_uuid: Int? = 0,
    val uuid: Int? = 0
)