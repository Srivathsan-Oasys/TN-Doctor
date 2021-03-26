package com.hmis_tn.doctor.ui.emr_workflow.investigation.models

data class Detail(
    val chief_complaint_uuid: Int = 0,
    val diagnosis_uuid: Int = 0,
    val display_order: String = "",
    val drug_frequency_uuid: Int = 0,
    val drug_instruction_uuid: Int = 0,
    val drug_route_uuid: Int = 0,
    val duration: Int = 0,
    val duration_period_uuid: Int = 0,
    val is_active: Boolean = false,
    val item_master_uuid: Int = 0,
    val revision: Boolean = false,
    val test_master_type_uuid: Int = 0,
    val test_master_uuid: Int = 0,
    val vital_master_uuid: Int = 0
)