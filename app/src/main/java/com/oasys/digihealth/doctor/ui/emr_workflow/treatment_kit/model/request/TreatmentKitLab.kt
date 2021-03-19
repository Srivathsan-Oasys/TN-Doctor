package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.request

data class TreatmentKitLab(
    var test_master_uuid: Int? = 0,
    var profile_master_uuid: String? = "",
    var order_to_location_uuid: Int? = 0,
    var order_priority_uuid: String? = ""
)