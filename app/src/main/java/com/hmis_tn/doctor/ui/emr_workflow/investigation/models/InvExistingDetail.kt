package com.hmis_tn.doctor.ui.emr_workflow.investigation.models

data class InvExistingDetail(
    var details_comments: String? = null,
    var order_priority_uuid: Int? = null,
    var profile_uuid: Int? = null,
    var patient_order_uuid: Int? = null,
    var to_location_uuid: Int? = null,
    var test_master_uuid: Int? = null,
    var uuid: Int? = 530
)