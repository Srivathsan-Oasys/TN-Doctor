package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response

data class TreatmentKitDrug(
    val created_by: String? = "",
    val created_date: String? = "",
    val drug_frequency_uuid: Any? = Any(),
    val drug_instruction_uuid: String? = "",
    val drug_route_uuid: String? = "",
    val duration: String? = "",
    val duration_period_uuid: String? = "",
    val is_active: Int? = 0,
    val item_master_uuid: Int? = 0,
    val modified_by: String? = "",
    val modified_date: String? = "",
    val quantity: String? = "",
    val revision: Int? = 0,
    val status: Int? = 0,
    val treatment_kit_uuid: Int? = 0,
    val user_uuid: String? = ""
)