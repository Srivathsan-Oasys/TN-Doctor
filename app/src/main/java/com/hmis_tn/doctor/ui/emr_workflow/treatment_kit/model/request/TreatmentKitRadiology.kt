package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.request

data class TreatmentKitRadiology(
    var test_master_uuid: Int? = 0,
    var profile_master_uuid: String? = "",
    var order_to_location_uuid: String? = "",
    var order_priority_uuid: String? = "",
    var radiology_description: String? = ""
)