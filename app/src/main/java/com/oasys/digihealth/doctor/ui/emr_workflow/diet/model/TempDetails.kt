package com.oasys.digihealth.doctor.ui.emr_workflow.diet.model

data class TempDetails(
    val created_by: String,
    val created_date: String,
    val department_name: String,
    val display_order: Int,
    val facility_name: String,
    val is_public: Boolean,
    val modified_by: String,
    val modified_date: String,
    val template_department: Int,
    val template_desc: String,
    val template_id: Int,
    val template_name: String,
    val user_uuid: Int
)