package com.hmis_tn.doctor.ui.emr_workflow.anesthesia_notes.model

data class ProfileSectionCategoryConcept(
    var code: String?,
    var description: String?,
    var display_order: Int?,
    var is_mandatory: Boolean?,
    var is_multiple: Boolean?,
    var name: String?,
    var profile_section_category_concept_values: List<ProfileSectionCategoryConceptValue>?,
    var profile_section_category_uuid: Int?,
    var uuid: Int?,
    var value_type_uuid: Int?,
    var value_types: ValueTypes?
)