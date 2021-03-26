package com.hmis_tn.doctor.ui.emr_workflow.ip_case_sheet.model

data class ReqContent(
    var activity_uuid: Any?,
    var category_key: String?,
    var category_uuid: Int?,
    var comments: String?,
    var concept_key: String?,
    var concept_uuid: Int?,
    var consultation_uuid: Int?,
    var encounter_doctor_uuid: Int?,
    var encounter_uuid: Int?,
    var entry_date: String?,
    var patient_uuid: String?,
    var profile_section_category_concept_uuid: Int?,
    var profile_section_category_concept_value_uuid: Int?,
    var profile_section_category_uuid: Int?,
    var profile_section_uuid: Int?,
    var profile_type_uuid: Int?,
    var profile_uuid: Int?,
    var result_binary: String?,
    var result_path: String?,
    var result_value: Int?,
    var result_value_json: String?,
    var result_value_rich_text: String?,
    var section_key: String?,
    var section_uuid: Int?,
    var term_key: String?
)