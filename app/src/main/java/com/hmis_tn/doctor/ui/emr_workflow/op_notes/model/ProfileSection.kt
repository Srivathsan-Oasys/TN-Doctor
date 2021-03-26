package com.hmis_tn.doctor.ui.emr_workflow.op_notes.model

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