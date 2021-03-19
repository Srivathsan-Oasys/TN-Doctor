package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery.save

data class ResponseContentsX(
    val anaesthetic_nurse_uuid: Int? = 0,
    val anaesthetist_uuid: Int? = 0,
    val anesthesia_type_uuid: Int? = 0,
    val approach: String? = "",
    val assistant_nurse_uuid: Int? = 0,
    val assistant_surgeon_uuid: Int? = 0,
    val body_side_uuid: Int? = 0,
    val body_site_uuid: Int? = 0,
    val cheif_uuid: Int? = 0,
    val comments: String? = "",
    val created_by: String? = "",
    val created_date: String? = "",
    val department_uuid: String? = "",
    val diagnosis_uuid: Int? = 0,
    val doctor_uuid: Int? = 0,
    val encounter_uuid: Int? = 0,
    val end_date: String? = "",
    val facility_uuid: String? = "",
    val incision: String? = "",
    val instructions: String? = "",
    val is_active: Boolean? = false,
    val is_force_booking: Boolean? = false,
    val lesion_uuid: Int? = 0,
    val modified_by: String? = "",
    val modified_date: String? = "",
    val notes: String? = "",
    val ot_grade_uuid: Int? = 0,
    val ot_master_uuid: String? = "",
    val ot_priority_uuid: Int? = 0,
    val ot_schedule_status_uuid: Int? = 0,
    val ot_scheduled_on: String? = "",
    val ot_team_uuid: Int? = 0,
    val ot_type_uuid: String? = "",
    val patient_uuid: String? = "",
    val position_uuid: Int? = 0,
    val procedure_category_uuid: Int? = 0,
    val procedure_type_uuid: Int? = 0,
    val procedure_uuid: Int? = 0,
    val revision: Boolean? = false,
    val scrub_nurse_uuid: Int? = 0,
    val start_date: String? = "",
    val status: Boolean? = false,
    val surgeon_uuid: Int? = 0,
    val uuid: Int? = 0,
    val ward_bed_mapping_uuid: Int? = 0,
    val ward_master_uuid: Int? = 0
)