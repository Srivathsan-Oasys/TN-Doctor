package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.modify

data class ModifyOtScheduleReq(
    var Id: Int?,
    var anaesthetic_nurse_uuid: Int?,
    var anaesthetist_uuid: Int?,
    var anesthesia_type_uuid: Int?,
    var approach: String?,
    var assistant_nurse_uuid: Int?,
    var assistant_surgeon_uuid: Int?,
    var body_side_uuid: Int?,
    var body_site_uuid: Int?,
    var cheif_uuid: Int?,
    var comments: String?,
    var department_uuid: Int?,
    var diagnosis_uuid: Int?,
    var doctor_uuid: Int?,
    var encounter_uuid: Int?,
    var end_date: String?,
    var facility_uuid: Int?,
    var incision: String?,
    var instructions: String?,
    var is_force_booking: Boolean?,
    var lesion_uuid: Int?,
    var notes: String?,
    var ot_grade_uuid: Int?,
    var ot_master_uuid: Int?,
    var ot_priority_uuid: Int?,
    var ot_schedule_status_uuid: Int?,
    var ot_scheduled_on: String?,
    var ot_team_uuid: Int?,
    var ot_type_uuid: Int?,
    var position_uuid: Int?,
    var procedure_category_uuid: Int?,
    var procedure_type_uuid: Int?,
    var procedure_uuid: Int?,
    var scrub_nurse_uuid: Int?,
    var start_date: String?,
    var surgeon_uuid: Int?,
    var ward_bed_mapping_uuid: Int?,
    var ward_master_uuid: Int?
)