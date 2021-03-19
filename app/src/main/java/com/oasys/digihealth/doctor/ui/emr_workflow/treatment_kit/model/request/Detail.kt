package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.request

data class Detail(
    var comments: String? = "",
    var encounter_type_uuid: Int? = 0,
    var group_uuid: Int? = 0,
    var is_active: Int? = 0,
    var is_profile: Boolean? = false,
    var lab_master_type_uuid: Int? = 0,
    var order_priority_uuid: String? = "",
    var patient_work_order_by: Int? = 0,
    var profile_uuid: Int? = 0,
    var status: Int? = 0,
    var test_master_uuid: Int? = 0,
    var to_department_uuid: Int? = 0,
    var to_location_uuid: String? = "",
    var application_type_uuid: Int? = null,
    var ward_uuid: Int? = null,
    var to_sub_department_uuid: Int? = 0
)