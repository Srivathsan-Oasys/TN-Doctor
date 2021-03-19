package com.oasys.digihealth.doctor.ui.emr_workflow.diet.model

data class DietHeaders(
    val active_from: String,
    val created_by: String,
    val created_date: String,
    val department_uuid: String,
    val diagnosis_uuid: Int,
    val display_order: String,
    val facility_uuid: String,
    val favourite_type_uuid: Int,
    val is_active: Int,
    val is_public: Boolean,
    val modified_by: String,
    val modified_date: String,
    val revision: Int,
    val status: Int,
    val ticksheet_type_uuid: Int,
    val user_uuid: String,
    val uuid: Int
)