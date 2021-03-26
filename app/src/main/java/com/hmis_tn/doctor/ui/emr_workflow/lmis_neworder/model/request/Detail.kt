package com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.model.request

data class Detail(
    var confidential_uuid: String? = "",
    var group_uuid: Int? = 0,
    var is_active: Boolean? = false,
    var is_approval_requried: Boolean? = false,
    var is_confidential: Boolean? = false,
    var is_profile: Boolean? = false,
    var lab_master_type_uuid: Int? = 0,
    var order_priority_uuid: Int? = 0,
    var profile_uuid: Int? = null,
    var sample_type_uuid: String? = "",

    var tat_session_end: String? = "",
    var tat_session_start: String? = "",
    var test_diseases_uuid: String? = "",
    var test_master_uuid: Int? = null,
    var to_department_uuid: Int? = 0,
    var to_location_uuid: String? = "",
    var to_sub_department_uuid: Int? = 0,
    var application_type_uuid: Int? = null,
    var ward_uuid: Int? = null,
    var type_of_method_uuid: String? = ""
)