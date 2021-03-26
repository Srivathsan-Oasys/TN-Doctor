package com.hmis_tn.doctor.ui.emr_workflow.vitals.model

data class TemplateMasterDetail(
    val chief_complaint_uuid: Int = 0,
    val comments: String = "",
    val created_by: Int = 0,
    val created_date: String = "",
    val diet_category_uuid: Int = 0,
    val diet_frequency_uuid: Int = 0,
    val diet_master_uuid: Int = 0,
    val display_order: Int = 0,
    val drug_frequency_uuid: Int = 0,
    val drug_instruction_uuid: Int = 0,
    val drug_route_uuid: Int = 0,
    val duration: Int = 0,
    val duration_period_uuid: Int = 0,
    val is_active: Boolean = false,
    val item_master_uuid: Int = 0,
    val modified_by: Int = 0,
    val modified_date: String = "",
    val quantity: Int = 0,
    val revision: Int = 0,
    val status: Boolean = false,
    val template_master_uuid: Int = 0,
    var test_master_uuid: Int = 0,
    var itemAppendString: String? = "",
    var vitalUUID: Int = 0,
    var templateMasterDetailsRowID: Int = 0,
    val uuid: Int = 0,
    val vital_master: VitalTemplateMaster = VitalTemplateMaster(),
    val vital_master_uuid: Int = 0
)