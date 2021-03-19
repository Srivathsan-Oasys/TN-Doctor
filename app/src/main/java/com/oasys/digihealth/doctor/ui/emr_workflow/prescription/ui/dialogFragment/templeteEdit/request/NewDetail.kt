package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.dialogFragment.templeteEdit.request

data class NewDetail(
    var chief_complaint_uuid: Int = 0,
    var display_order: Int = 0,
    var drug_duration: String = "",
    var drug_frequency_uuid: String = "",
    var drug_id: Int = 0,
    var drug_instruction_uuid: String = "",
    var drug_period_uuid: String = "",
    var drug_route_uuid: String = "",
    var is_active: Boolean = true,
    var quantity: Int = 0,
    var revision: Boolean = true,
    var template_master_uuid: Int = 0,
    var test_master_uuid: Int = 0,
    var vital_master_uuid: Int = 0
)