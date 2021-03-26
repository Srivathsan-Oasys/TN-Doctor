package com.hmis_tn.doctor.ui.emr_workflow.certificate.model

data class TemplateItemResponseContents(
    val code: String = "",
    val created_by: Any = Any(),
    val created_by_id: Int = 0,
    val created_date: String = "",
    val data_template: String = "",
    val department_name: String = "",
    val department_uuid: Int = 0,
    val facility_name: String = "",
    val facility_uuid: Int = 0,
    val is_active: Boolean = false,
    val is_default: Boolean = false,
    val modified_by: Any = Any(),
    val modified_by_id: Int = 0,
    val modified_date: String = "",
    val name: String = "",
    val note_template_type: NoteTemplateType = NoteTemplateType(),
    val note_template_type_uuid: Int = 0,
    val note_type: Any = Any(),
    val note_type_uuid: Int = 0,
    val revision: Int = 0,
    val status: Boolean = false,
    val uuid: Int = 0
)