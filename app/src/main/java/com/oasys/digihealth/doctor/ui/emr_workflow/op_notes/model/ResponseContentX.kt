package com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model

data class ResponseContentX(
    var department_uuid: Int?,
    var profile_code: String?,
    var profile_description: String?,
    var profile_name: String?,
    var profile_sections: List<ProfileSection>?,
    var profile_type_uuid: Int?,
    var uuid: Int?
)