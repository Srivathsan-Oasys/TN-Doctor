package com.hmis_tn.doctor.ui.emr_workflow.diet.model

data class DietDetail(
    val chief_complaint_uuid: Int,
    val created_by: String,
    val created_date: String,
    val diagnosis_uuid: Int,
    val diet_category_uuid: String,
    val diet_frequency_uuid: String,
    val diet_master_uuid: Int,
    val display_order: String,
    val drug_frequency_uuid: Int,
    val drug_instruction_uuid: Int,
    val drug_route_uuid: Int,
    val duration: Int,
    val duration_period_uuid: Int,
    val favourite_master_uuid: Int,
    val favourite_type_uuid: Int,
    val is_active: Int,
    val item_master_uuid: Int,
    val modified_by: String,
    val modified_date: String,
    val quantity: String,
    val revision: Int,
    val status: Int,
    val test_master_type_uuid: Int,
    val test_master_uuid: Int,
    val user_uuid: String,
    val uuid: Int,
    val vital_master_uuid: Int


)