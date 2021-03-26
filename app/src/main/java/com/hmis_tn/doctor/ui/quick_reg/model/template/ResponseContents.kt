package com.hmis_tn.doctor.ui.quick_reg.model.template

data class ResponseContents(
    val code: String?,
    val created_by: String?,
    val created_by_id: Int?,
    val created_date: String?,
    val data_template: String?,
    val department_name: Any?,
    val department_uuid: Int?,
    val facility_name: String?,
    val facility_uuid: Int?,
    val is_active: Boolean?,
    val is_default: Boolean?,
    val modified_by: String?,
    val modified_by_id: Int?,
    val modified_date: String?,
    val name: String?,
    val note_template_type: NoteTemplateType?,
    val note_template_type_uuid: Int?,
    val note_type: Any?,
    val note_type_uuid: Int?,
    val revision: Int?,
    val status: Boolean?,
    val uuid: Int?
)