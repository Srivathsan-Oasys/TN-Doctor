package com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.model.response

data class ChiefComplaintResponseContent(
    val chief_complaint_duration: String = "",
    val chief_complaint_duration_period_uuid: String = "",
    val chief_complaint_uuid: Int = 0,
    val consultation_uuid: Int = 0,
    val created_by: String = "",
    val created_date: String = "",
    val encounter_type_uuid: Int = 0,
    val encounter_uuid: Int = 0,
    val is_active: Int = 0,
    val modified_by: String = "",
    val modified_date: String = "",
    val patient_uuid: String = "",
    val revision: Int = 0,
    val status: Int = 0,
    val tat_end_time: String = "",
    val tat_start_time: String = "",
    val user_uuid: String = "",
    val uuid: Int = 0
)