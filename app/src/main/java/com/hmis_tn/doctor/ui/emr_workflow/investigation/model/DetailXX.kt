package com.hmis_tn.doctor.ui.emr_workflow.investigation.model

data class DetailXX(
    var group_uuid: Int = 0,
    var is_profile: Boolean = false,
    var lab_master_type_uuid: Int = 0,
    var order_priority_uuid: String = "",
    var profile_uuid: String = "",
    var test_master_uuid: Int = 0,
    var to_department_uuid: Int = 0,
    var to_location_uuid: String = "",
    var to_sub_department_uuid: Int = 0
)