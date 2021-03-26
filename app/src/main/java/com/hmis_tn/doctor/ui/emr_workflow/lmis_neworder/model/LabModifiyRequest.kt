package com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.model

data class LabModifiyRequest(
    var existing_details: ArrayList<ExistingDetail?>? = ArrayList(),
    var new_details: ArrayList<NewDetail?>? = ArrayList(),
    var removed_details: ArrayList<RemovedDetail?>? = ArrayList()
) {
    data class ExistingDetail(
        var application_type_uuid: Int? = null,
        var details_comments: String? = null,
        var order_priority_uuid: Any? = null,
        var patient_order_uuid: Int? = null,
        var profile_uuid: Any? = null,
        var test_master_uuid: Int? = null,
        var to_location_uuid: Int? = null,
        var type_of_method_uuid: Int? = null,
        var uuid: Int? = null,
        var ward_uuid: String? = null
    )

    data class NewDetail(
        var application_type_uuid: Int? = null,
        var doctor_uuid: String? = null,
        var encounter_type_uuid: Int? = null,
        var encounter_uuid: Int? = null,
        var from_department_uuid: String? = null,
        var group_uuid: Int? = null,
        var is_ordered: Boolean? = null,
        var is_profile: Boolean? = null,
        var lab_master_type_uuid: Int? = null,
        var order_priority_uuid: Int? = null,
        var order_request_date: String? = null,
        var order_status_uuid: Int? = null,
        var patient_order_uuid: Int? = null,
        var patient_uuid: String? = null,
        var patient_work_order_by: Int? = null,
        var profile_uuid: Any? = null,
        var test_master_uuid: Int? = null,
        var to_department_uuid: Int? = null,
        var to_location_uuid: Int? = null,
        var to_sub_department_uuid: Int? = null,
        var ward_uuid: String? = null
    )

    data class RemovedDetail(
        var patient_orders_uuid: Int? = null,
        var profile_uuid: Any? = null,
        var test_master_uuid: Int? = null,
        var uuid: Int? = null
    )
}