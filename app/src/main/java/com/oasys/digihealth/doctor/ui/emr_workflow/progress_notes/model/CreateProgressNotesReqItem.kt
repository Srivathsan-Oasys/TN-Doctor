package com.oasys.digihealth.doctor.ui.emr_workflow.progress_notes.model

data class CreateProgressNotesReqItem(
    var captured_by: String?,
    var captured_on: String?,
    var daily_note: String?,
    var department_uuid: String?,
    var encounter_type_uuid: Int?,
    var encounter_uuid: Int?,
    var facility_uuid: String?,
    var is_phr: Boolean?,
    var note_status_uuid: Int?,
    var patient_uuid: Int?,
    var progressnote_type_uuid: Int?,
    var special_note: String?
)