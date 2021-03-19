package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.modify

data class ExistingDetail(
    var details_comments: String? = "",
    var order_priority_uuid: String? = "",
    var patient_order_uuid: Int? = 0,
    var to_location_uuid: Int? = 0,
    var uuid: Int? = 0
)