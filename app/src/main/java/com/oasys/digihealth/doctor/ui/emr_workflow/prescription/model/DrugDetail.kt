package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model

data class DrugDetail(
    var drug_code: String = "",
    var drug_duration: Int = 0,
    var drug_frequency_id: Int = 0,
    var drug_frequency_name: String = "",
    var drug_id: Int = 0,
    var ischeck: Boolean = false,
    var duration_period_uuid: Int = 0,
    var drug_instruction_code: String = "",
    var drug_instruction_id: Int = 0,
    var drug_instruction_name: String = "",
    var drug_name: String = "",
    var drug_period_code: String = "",
    var drug_period_id: Int = 0,
    var drug_period_name: String = "",
    var drug_route_id: Int = 0,
    var drug_route_name: String = "",
    var is_active: Boolean = false,
    var drug_is_emar: Boolean = false,
    var template_details_displayorder: Int = 0,
    var template_details_uuid: Int = 0,
    var isSelected: Boolean = false
)