package com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.prev_records

data class ResponseContent(
    var Count: Int?,
    var consultation_uuid: Int?,
    var created_by: Int?,
    var created_date: String?,
    var encounter_doctor_uuid: Int?,
    var encounter_type_uuid: Any?,
    var encounter_uuid: Int?,
    var is_active: Boolean?,
    var modified_by: Int?,
    var modified_date: String?,
    var patient_uuid: Int?,
    var profile: Profile?,
    var profile_uuid: Int?,
    var status: Boolean?,
    var uuid: Int?
)