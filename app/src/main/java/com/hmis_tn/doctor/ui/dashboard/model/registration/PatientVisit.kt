package com.hmis_tn.doctor.ui.dashboard.model.registration

data class PatientVisit(
    val created_by: Int = 0,
    val created_date: String = "",
    val department_uuid: Int = 0,
    val facility_uuid: Int = 0,
    val is_active: Boolean = false,
    val is_last_visit: Boolean = false,
    val is_mlc: Boolean = false,
    val modified_by: Int = 0,
    val modified_date: String = "",
    val patient_type_uuid: Int = 0,
    val patient_uuid: Int = 0,
    val registered_date: String = "",
    val revision: Boolean = false,
    val session_uuid: Int = 0,
    val speciality_department_uuid: Int = 0,
    val unit_uuid: Int = 0,
    val uuid: Int = 0,
    val visit_number: String = "",
    val visit_type_uuid: Int = 0
)