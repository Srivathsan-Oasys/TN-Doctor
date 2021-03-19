package com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model

data class ProfileSection(
    var activity_uuid: Int?,
    var display_order: Int?,
    var profile_section_categories: List<ProfileSectionCategory>?,
    var profile_uuid: Int?,
    var section_uuid: Int?,
    var sections: Sections?,
    var uuid: Int?
) {
    var isSelected = false
}