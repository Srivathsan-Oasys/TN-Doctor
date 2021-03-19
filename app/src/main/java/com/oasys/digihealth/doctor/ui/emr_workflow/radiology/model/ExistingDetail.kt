package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model

data class ExistingDetail(
    var details_comments: String = "",
    var test_master_uuid: Int = 0,
    var order_priority_uuid: Int = 0,
    var patient_order_uuid: Int = 0,
    var to_location_uuid: Int = 0,
    var uuid: String = ""
)