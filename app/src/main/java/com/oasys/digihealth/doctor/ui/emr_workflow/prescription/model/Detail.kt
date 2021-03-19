package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model

data class Detail(
    var comments: String? = "",
    var drug_frequency_uuid: String? = "",
    var drug_instruction_uuid: String? = "1",
    var drug_route_uuid: String? = "",
    var injection_room_uuid: String? = "0",
    var duration: String? = "",
    var duration_period_uuid: String? = "",
    var is_emar: Boolean? = false,
    var item_master_uuid: Int? = 0,
    var prescribed_quantity: String? = "0.00",
    var administer_detail_status_uuid: Int? = 0
)