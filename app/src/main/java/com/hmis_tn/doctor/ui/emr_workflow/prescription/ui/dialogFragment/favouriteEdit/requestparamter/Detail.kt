package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.favouriteEdit.requestparamter

data class Detail(
    var chief_complaint_uuid: Int? = 0,
    var comments: String? = "",
    var diagnosis_uuid: Int? = 0,
    var display_order: String? = "",
    var drug_frequency_uuid: String? = "",
    var drug_instruction_uuid: String? = "",
    var drug_route_uuid: String? = "",
    var duration: String? = "",
    var duration_period_uuid: String? = "",
    var injection_room_uuid: String? = "",
    var is_active: String? = "",
    var item_master_uuid: Int? = 0,
    var revision: Boolean? = false,
    var test_master_type_uuid: Int? = 0,
    var test_master_uuid: Int? = 0,
    var vital_master_uuid: Int? = 0
)