package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.request

data class DetailXXX(
    var details_comments: String? = "",
    var encounter_type_uuid: Int? = 0,
    var group_uuid: Int? = 0,
    var is_profile: Boolean? = false,
    var lab_master_type_uuid: Int? = 0,
    var order_priority_uuid: String? = "",
    var profile_uuid: Int? = 0,
    var test_master_uuid: Int? = 0,
    var to_department_uuid: Int? = 0,
    var to_location_uuid: String? = "",
    var to_sub_department_uuid: Int? = 0
)