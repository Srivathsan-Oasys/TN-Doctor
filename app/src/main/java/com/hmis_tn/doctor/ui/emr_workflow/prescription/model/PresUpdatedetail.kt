package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

data class PresUpdatedetail(
    var comments: String? = null,
    var drug_frequency_uuid: Int? = null,
    var drug_instruction_uuid: Int? = null,
    var drug_route_uuid: Int? = null,
    var duration: Int? = null,
    var duration_period_uuid: Int? = null,
    var is_emar: Boolean? = null,
    var item_master_uuid: Int? = null,
    var prescribed_quantity: String? = null,
    var prescription_uuid: Int? = null,
    var uuid: Int? = null
)