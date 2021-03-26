package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.response

data class Req(
    val admission_facility_uuid: String? = "",
    val admission_status_uuid: Int? = 0,
    val admitting_reason_uuid: Int? = 0,
    val created_by: String? = "",
    val created_date: String? = "",
    val department_uuid: String? = "",
    val doctor_uuid: String? = "",
    val encounter_uuid: Int? = 0,
    val from_facility: String? = "",
    val is_active: Int? = 0,
    val modified_by: String? = "",
    val modified_date: String? = "",
    val patient_uuid: String? = "",
    val status: Int? = 0,
    val ward_uuid: String? = ""
)