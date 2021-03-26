package com.hmis_tn.doctor.ui.emr_workflow.radiology.model

data class DetailX(
    var chief_complaint_uuid: Int = 0,
    var created_by: String = "",
    var created_date: String = "",
    var drug_frequency_uuid: Int = 0,
    var drug_instruction_uuid: Int = 0,
    var drug_route_uuid: Int = 0,
    var duration: Int = 0,
    var duration_period_uuid: Int = 0,
    var is_active: Boolean = false,
    var item_master_uuid: Int = 0,
    val modified_by: Int = 0,
    var modified_date: String = "",
    var revision: Boolean = false,
    var template_master_uuid: Int = 0,
    var test_master_uuid: Int = 0,
    var vital_master_uuid: Int = 0
)