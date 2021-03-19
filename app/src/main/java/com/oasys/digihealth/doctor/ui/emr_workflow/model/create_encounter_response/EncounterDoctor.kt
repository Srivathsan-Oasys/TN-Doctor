package com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response

data class EncounterDoctor(
    val consultation_start_date: String? = null,
    val created_by: String? = null,
    val created_date: String? = null,
    val department_uuid: String? = null,
    val dept_visit_type_uuid: Int? = null,
    val doctor_uuid: String? = null,
    val doctor_visit_type_uuid: Int? = null,
    val encounter_uuid: Int? = null,
    val is_active: Int? = null,
    val modified_by: String? = null,
    val modified_date: String? = null,
    val patient_uuid: String? = null,
    val revision: Int? = 0,
    val session_type_uuid: Int? = null,
    val speciality_uuid: Int? = null,
    val status: Int? = null,
    val sub_deparment_uuid: Int? = null,
    val tat_end_time: String? = null,
    val tat_start_time: String? = null,
    val user_uuid: String? = null,
    val uuid: Int? = null,
    val visit_type_uuid: Int? = null
)