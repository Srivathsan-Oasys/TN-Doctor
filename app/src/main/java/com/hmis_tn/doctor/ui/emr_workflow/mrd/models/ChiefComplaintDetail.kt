package com.hmis_tn.doctor.ui.emr_workflow.mrd.models

data class ChiefComplaintDetail(
    val cheif_complaint_code: String = "",
    val cheif_complaint_desc: String = "",
    val cheif_complaint_name: String = "",
    val cheif_complaint_performed_date: String = "",
    val cheif_complaint_uuid: Int = 0,
    val chief_complaint_duration: Int = 0,
    val chief_complaint_duration_period_code: String = "",
    val chief_complaint_duration_period_name: String = "",
    val chief_complaint_duration_period_uuid: Int = 0,
    val patient_cheif_complaint_uuid: Int = 0
)