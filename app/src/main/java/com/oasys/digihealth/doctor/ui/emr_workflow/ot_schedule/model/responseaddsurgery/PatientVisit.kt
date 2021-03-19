package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery

data class PatientVisit(
    val created_by: Int? = 0,
    val created_date: String? = "",
    val department_uuid: Int? = 0,
    val facility_uuid: Int? = 0,
    val is_active: Boolean? = false,
    val is_last_visit: Boolean? = false,
    val is_mlc: Any? = Any(),
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    val patient_type_uuid: Int? = 0,
    val patient_uuid: Int? = 0,
    val registered_date: String? = "",
    val revision: Boolean? = false,
    val session_uuid: Int? = 0,
    val speciality_department_uuid: Any? = Any(),
    val unit_uuid: Any? = Any(),
    val uuid: Int? = 0,
    val visit_number: String? = "",
    val visit_type_uuid: Int? = 0
)