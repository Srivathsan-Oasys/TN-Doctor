package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

data class PrescriptionDetailsResult(
    var comments: String? = "",
    var created_by: String? = "",
    var created_date: String? = "",
    var drug_frequency_uuid: String? = "",
    var drug_instruction_uuid: Int? = 0,
    var drug_route_uuid: Int? = 0,
    var duration: Int? = 0,
    var duration_period_uuid: Int? = 0,
    var is_active: Boolean? = false,
    var is_added_to_treatment_plan: Boolean? = false,
    var is_emar: Boolean? = false,
    var item_master_uuid: Int? = 0,
    var modified_by: String? = "",
    var modified_date: String? = "",
    var prescribed_quantity: Int? = 0,
    var prescription_uuid: Int? = 0,
    var revision: Int? = 0,
    var status: Boolean? = false,
    var uuid: Int? = 0
)