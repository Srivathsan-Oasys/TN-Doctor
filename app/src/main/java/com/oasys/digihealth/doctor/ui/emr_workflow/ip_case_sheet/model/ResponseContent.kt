package com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model

data class ResponseContent(
    var created_by: Int?,
    var created_date: String?,
    var department_uuid: Int?,
    var facility_uuid: Int?,
    var is_active: Boolean?,
    var modified_by: Int?,
    var modified_date: String?,
    var profile_code: String?,
    var profile_description: String?,
    var profile_name: String?,
    var profile_type_uuid: Int?,
    var revision: Int?,
    var status: Boolean?,
    var uuid: Int?
)