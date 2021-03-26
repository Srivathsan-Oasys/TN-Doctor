package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.modify

data class NewDetail(
    var details_comments: String? = "",
    var doctor_uuid: String? = "",
    var encounter_type_uuid: String? = "",
    var encounter_uuid: String? = "",
    var from_department_uuid: String? = "",
    var group_uuid: Int? = 0,
    var is_ordered: Boolean? = false,
    var is_profile: Boolean? = false,
    var lab_master_type_uuid: Int? = 0,
    var order_priority_uuid: String? = "",
    var order_request_date: String? = "",
    var order_status_uuid: Int? = 0,
    var patient_uuid: String? = "",
    var patient_work_order_by: Int? = 0,
    var profile_uuid: Any? = Any(),
    var sample_type_uuid: String? = "",
    var test_master_uuid: Int? = 0,
    var to_department_uuid: Int? = 0,
    var to_location_uuid: Int? = 0,
    var to_sub_department_uuid: Int? = 0,
    var type_of_method_uuid: Int? = 0
)