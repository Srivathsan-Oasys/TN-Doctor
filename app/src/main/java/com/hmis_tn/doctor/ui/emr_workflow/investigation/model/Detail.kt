package com.hmis_tn.doctor.ui.emr_workflow.investigation.model

data class Detail(
    val chief_complaint_uuid: Int = 0,
    val created_by: String = "",
    val created_: String = "",
    val diagnosis_uuid: Int = 0,
    val display_order: String = "",
    val drug_frequency_uuid: Int = 0,
    val drug_instruction_uuid: Int = 0,
    val drug_route_uuid: Int = 0,
    val duration: Int = 0,
    val duration_period_uuid: Int = 0,
    val favourite_master_uuid: Int = 0,
    val is_active: Int = 0,
    val item_master_uuid: Int = 0,
    val modified_by: String = "",
    val modified_date: String = "",
    val revision: Int = 0,
    val status: Int = 0,
    val test_master_type_uuid: Int = 0,
    val test_master_uuid: Int = 0,
    val user_uuid: String = "",
    val uuid: Int = 0,
    val vital_master_uuid: Int = 0
)