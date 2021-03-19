package com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.get_default

data class ResponseContent(
    var created_by: Int?,
    var created_date: String?,
    var is_active: Boolean?,
    var modified_by: Int?,
    var modified_date: String?,
    var profile_type_uuid: Int?,
    var profile_uuid: Int?,
    var revision: Revision?,
    var status: Boolean?,
    var user_uuid: Int?,
    var uuid: Int?
)