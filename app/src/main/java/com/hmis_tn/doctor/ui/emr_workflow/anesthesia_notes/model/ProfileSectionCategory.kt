package com.hmis_tn.doctor.ui.emr_workflow.anesthesia_notes.model

data class ProfileSectionCategory(
    var categories: Categories?,
    var category_uuid: Int?,
    var display_order: Int?,
    var profile_section_category_concepts: List<ProfileSectionCategoryConcept>?,
    var profile_section_uuid: Int?,
    var uuid: Int?
)