package com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.prev_records

data class Profile(
    var created_date: String?,
    var department_uuid: Int?,
    var facility_uuid: Int?,
    var profile_code: String?,
    var profile_description: String?,
    var profile_name: String?,
    var profile_type_uuid: Int?,
    var uuid: Int?
)