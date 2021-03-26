package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model

data class ResponseContent(
    var admission_request_uuid: Int?,
    var admission_uuid: Int?,
    var appointment_uuid: Int?,
    var created_by: Int?,
    var created_date: String?,
    var department_uuid: Int?,
    var discharge_date: Any?,
    var discharge_type_uuid: Int?,
    var encounter_date: String?,
    var encounter_doctors: List<EncounterDoctor>?,
    var encounter_identifier: Int?,
    var encounter_priority_uuid: Int?,
    var encounter_status_uuid: Int?,
    var encounter_type_uuid: Int?,
    var facility_uuid: Int?,
    var is_active: Boolean?,
    var is_active_encounter: Boolean?,
    var is_followup: Boolean?,
    var is_group_casuality: Boolean?,
    var is_mrd_request: Boolean?,
    var modified_by: Int?,
    var modified_date: String?,
    var patient_uuid: Int?,
    var revision: Boolean?,
    var status: Boolean?,
    var uuid: Int?
)