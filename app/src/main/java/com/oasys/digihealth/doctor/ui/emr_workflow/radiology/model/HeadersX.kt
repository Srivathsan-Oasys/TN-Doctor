package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model

data class HeadersX(
    var active_from: String = "",
    var active_to: String = "",
    var created_by: String = "",
    var created_date: String = "",
    var department_uuid: String = "",
    var description: String = "",
    var diagnosis_uuid: Int = 0,
    var display_order: String = "",
    var facility_uuid: String = "",
    var is_active: Boolean = false,

    var is_public: String = "",
    var modified_by: String = "",
    var modified_date: String = "",
    var name: String = "",
    var revision: Boolean = false,
    var status: Int = 0,
    var template_type_uuid: Int = 0,
    var user_uuid: String = ""
)