package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model

data class TempDetails(
    val created_by: Int = 0,
    val created_by_name: String = "",
    val created_date: String = "",
    val department_name: String = "",
    val facility_name: String = "",
    val facility_uuid: Int = 0,
    val is_public: Boolean = false,
    val modified_by: Int = 0,
    val modified_by_name: String = "",
    val modified_date: String = "",
    val template_department: Int = 0,
    val template_description: String = "",
    val template_displayorder: Int = 0,
    val template_id: Int = 0,
    val template_is_active: Boolean = false,
    val template_name: String = "",
    val template_status: Boolean = false,
    val template_type_name: String = "",
    val template_type_uuid: Int = 0,
    val user_uuid: Int = 0,
    var isSelected: Boolean? = false
)