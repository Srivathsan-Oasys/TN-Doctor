package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.request

data class DetailX(
    var drug_frequency_uuid: String? = "",
    var drug_instruction_uuid: String? = "",
    var drug_route_uuid: String? = "",
    var duration: String? = "0",
    var duration_period_uuid: String? = "",
    var item_master_uuid: Int? = 0,
    var prescribed_quantity: String? = "0",
    var comments: String = "",
    var is_emar: Boolean = false,
    var injection_room_uuid: Int = 0
)