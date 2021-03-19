package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.requset_model

data class Detail(
    var group_uuid: Int = 0,
    var is_profile: Boolean = false,
    var lab_master_type_uuid: Int = 0,
    var order_priority_uuid: String = "",
    var profile_uuid: String = "",
    var test_master_uuid: Int = 0,
    var to_department_uuid: String = "",
    var to_location_uuid: String = "",
    var to_sub_department_uuid: Int = 0
)