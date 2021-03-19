package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.models

data class InvNewDetail(
    var diagnosis_uuid: Int? = null,
    var doctor_uuid: Int? = null,
    var encounter_uuid: Int? = null,
    var from_department_uuid: Int? = null,
    var group_uuid: Int? = null,
    var is_ordered: Int? = null,
    var is_profile: Int? = null,
    var is_sample_rejected: Int? = null,
    var lab_master_type_uuid: Int? = 2,
    var order_priority_uuid: Int? = null,
    var order_request_date: String? = null,
    var order_status_uuid: Int? = 1,
    var patient_order_test_details_uuid: Int? = null,
    var patient_order_uuid: Int? = null,
    var profile_uuid: String? = null,
    var sample_identifier: String? = null,
    var sample_rejected_reason: String? = null,
    var schedule_date: Int? = null,
    var test_master_uuid: String? = null,
    var encounter_type_uuid: String? = null,
    var to_department_uuid: Int? = null,
    var to_location_uuid: Int? = null,
    var to_sub_department_uuid: Int? = null,
    var vendor_sample_uuid: Int? = null,
    var vendor_test_uuid: Int? = null
)