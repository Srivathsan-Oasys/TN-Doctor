package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

data class DetailX(
    var chief_complaint_uuid: Int? = 0,
    var comments: String? = "",
    var created_by: String? = "",
    var created_date: String? = "",
    var diagnosis_uuid: Int? = 0,
    var display_order: String? = "",
    var drug_frequency_uuid: String? = "",
    var drug_instruction_uuid: String? = "",
    var drug_route_uuid: String? = "",
    var duration: String? = "",
    var duration_period_uuid: String? = "",
    var favourite_master_uuid: Int? = 0,
    var injection_room_uuid: String? = "",
    var is_active: Int? = 0,
    var item_master_uuid: Int? = 0,
    var modified_by: String? = "",
    var modified_date: String? = "",
    var revision: Int? = 0,
    var status: Int? = 0,
    var test_master_type_uuid: Int? = 0,
    var test_master_uuid: Int? = 0,
    var user_uuid: String? = "",
    var uuid: Int? = 0,
    var vital_master_uuid: Int? = 0
)