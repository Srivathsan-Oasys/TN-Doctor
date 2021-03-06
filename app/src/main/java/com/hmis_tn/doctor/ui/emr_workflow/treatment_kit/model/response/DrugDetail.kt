package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response

data class DrugDetail(
    val drug_code: String? = "",
    val drug_duration: String? = "",
    val drug_frequency_code: String? = "",
    val drug_frequency_display: String? = "",
    val drug_frequency_id: Int? = 0,
    val drug_frequency_name: String? = "",
    val drug_id: Int? = 0,
    val drug_instruction_code: String? = "",
    val drug_instruction_id: Int? = 0,
    val drug_instruction_name: String? = "",
    val drug_name: String? = "",
    val drug_period_code: String? = "",
    val drug_period_id: Int? = 0,
    val drug_period_name: String? = "",
    val drug_quantity: String? = "",
    val drug_route_code: String? = "",
    val drug_route_id: Int? = 0,
    val drug_route_name: String? = ""
)