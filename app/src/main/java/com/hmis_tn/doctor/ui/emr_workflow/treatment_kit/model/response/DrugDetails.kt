package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response

data class DrugDetails(
    val prescription_status_name: String? = "",
    val prescription_status_code: String? = "",
    val drug_name: String? = "",
    val drug_code: String? = "",
    val comments: String? = "",
    val drug_route_name: String? = "",
    val drug_route_code: String? = "",
    val drug_frequency_name: String? = "",
    val drug_frequency_code: String? = "",
    val drug_period_name: String? = "",
    val drug_period_code: String? = "",
    val drug_instruction_code: String? = "",
    val drug_instruction_name: String? = "",
    val uuid: Int? = 0,
    val prescription_uuid: Int? = 0,
    val prescribed_quantity: Int? = 0,
    val prescription_status_uuid: Int? = 0,
    val item_master_uuid: Int? = 0,
    val drug_route_id: Int? = 0,
    val drug_frequency_id: Int? = 0,
    val drug_period_id: Int? = 0,
    val duration: Int? = 0,
    val drug_instruction_id: Int? = 0
)
