package com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.response

data class Admisionreq(
    val Id: Int? = 0,
    val admission_facility_uuid: String? = "",
    val admission_status_uuid: Int? = 0,
    val admitting_reason_uuid: Int? = 0,
    val comments: String? = "",
    val department_uuid: Int? = 0,
    val doctor_uuid: String? = "",
    val encounter_uuid: Int? = 0,
    val from_facility: String? = "",
    val modified_date: String? = "",
    val patient_uuid: String? = "",
    val requested_date: String? = "",
    val ward_uuid: Int? = 0
)