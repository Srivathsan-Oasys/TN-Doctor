package com.hmis_tn.doctor.ui.quick_reg.model.request

data class Detail(
    var confidential_uuid: Int = 0,
    var group_uuid: Int = 0,
    var is_active: Boolean = false,
    var is_approval_requried: Boolean = false,
    var is_confidential: Boolean = false,
    var is_profile: Boolean = false,
    var lab_master_type_uuid: Int = 0,
    var order_priority_uuid: Int = 0,
    var profile_uuid: String = "",
    var sample_type_uuid: Int = 0,
    var tat_session_end: String = "",
    var tat_session_start: String = "",
    var test_diseases_uuid: Int = 0,
    var test_master_uuid: Int = 0,
    var to_department_uuid: Int = 0,
    var to_location_uuid: Int = 0,
    var to_sub_department_uuid: Int = 0,
    var type_of_method_uuid: Int = 0
)