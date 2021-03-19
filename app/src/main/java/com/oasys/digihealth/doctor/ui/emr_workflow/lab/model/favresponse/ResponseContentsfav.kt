package com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse

data class ResponseContentsfav(
    var chief_complaint_code: Any? = Any(),
    var chief_complaint_id: Any? = Any(),
    var chief_complaint_name: String? = "",
    var diagnosis_code: String? = "",
    var diagnosis_description: String? = "",
    var diagnosis_id: Int? = 0,
    var diagnosis_name: String? = "",
    var drug_active: Boolean? = false,
    var drug_duration: Int? = 0,
    var drug_frequency_id: String? = "",
    var drug_frequency_name: String? = "",
    var drug_id: Any? = Any(),
    var drug_instruction_code: String? = "",
    var drug_instruction_id: String? = "",
    var drug_instruction_name: String? = "",
    var drug_is_emar: Any? = Any(),
    var drug_name: String? = "",
    var drug_period_code: Any? = Any(),
    var drug_period_id: String? = "",
    var drug_period_name: String? = "",
    var drug_route_id: String? = "",
    var drug_route_name: String? = "",
    var favourite_details_id: Int? = 0,
    var favourite_display_order: Int? = 0,
    var favourite_id: Int? = 0,
    var favourite_name: String? = "",
    var typeofstring: String? = "",
    var test_master_code: String? = "",
    var test_master_description: String? = "",
    var test_master_id: Int? = 0,
    var test_master_name: String? = "",
    var vital_master_name: String? = "",
    var vital_master_uom: Any? = Any(),
    var template_id: Int? = 0,
    var template_details_uuid: Int? = 0,
    var template_master_uuid: Int? = 0,
    var diet_category_code: String = "",
    var diet_category_id: Int = 0,
    var diet_category_name: String = "",
    var diet_frequency_code: String = "",
    var diet_frequency_id: Int = 0,
    var diet_frequency_name: String = "",
    var diet_master_code: String = "",
    var diet_master_id: Int = 0,
    var diet_master_name: String = "",
    var diet_quantity: Int = 0

)