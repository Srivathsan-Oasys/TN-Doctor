package com.hmis_tn.doctor.ui.emr_workflow.mrd.models

data class CheifComplaintsDetail(
    val chief_complaint_details: List<ChiefComplaintDetail> = listOf(),
    val consultation_uuid: Int = 0,
    val created_date: String = "",
    val department: String = "",
    val department_uuid: Int = 0,
    val encounter_type_code: String = "",
    val encounter_type_name: String = "",
    val encounter_type_uuid: Int = 0,
    val encounter_uuid: Int = 0,
    val institution: String = "",
    val institution_uuid: Int = 0,
    val patient_uuid: Int = 0,
    val performed_by_first_name: String = "",
    val performed_by_last_name: Any = Any(),
    val performed_by_middle_name: Any = Any(),
    val performed_by_title: String = ""
)