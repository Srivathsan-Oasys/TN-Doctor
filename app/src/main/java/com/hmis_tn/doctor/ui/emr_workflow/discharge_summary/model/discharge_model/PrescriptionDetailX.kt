package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model

data class PrescriptionDetailX(
    val administer_detail_status_uuid: Int = 0,
    val administred_quantity: Int = 0,
    val comments: String = "",
    val created_by: Int = 0,
    val created_date: String = "",
    val dispense_detail_status_uuid: Int = 0,
    val dispensed_quantity: Int = 0,
    val drug_frequency: DrugFrequency = DrugFrequency(),
    val drug_frequency_uuid: Int = 0,
    val drug_instruction: DrugInstruction = DrugInstruction(),
    val drug_instruction_uuid: Int = 0,
    val drug_route: DrugRoute = DrugRoute(),
    val drug_route_uuid: Int = 0,
    val duration: Int = 0,
    val duration_period: DurationPeriod = DurationPeriod(),
    val duration_period_uuid: Int = 0,
    val generic_uuid: Int = 0,
    val is_active: Boolean = false,
    val is_added_to_treatment_plan: Boolean = false,
    val is_emar: Boolean = false,
    val is_reusable: IsReusable = IsReusable(),
    val item_master: ItemMaster = ItemMaster(),
    val item_master_uuid: Int = 0,
    val modified_by: Int = 0,
    val modified_date: String = "",
    val prescribed_quantity: Int = 0,
    val prescription_uuid: Int = 0,
    val revision: Int = 0,
    val status: Boolean = false,
    val tat_end_time: Any = Any(),
    val tat_start_time: Any = Any(),
    val treatment_plan_drug_map_uuid: Int = 0,
    val uuid: Int = 0
)