package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

data class PrescriptionDetailsData(
    var comments: String? = "",
    var drug_frequency_uuid: String? = "",
    var drug_instruction_uuid: Int? = 0,
    var drug_route_uuid: Int? = 0,
    var duration: Int? = 0,
    var duration_period_uuid: Int? = 0,
    var item_master_uuid: Int? = 0,
    var prescribed_quantity: Int? = 0
)