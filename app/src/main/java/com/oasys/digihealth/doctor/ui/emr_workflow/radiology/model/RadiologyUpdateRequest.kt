package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model

data class RadiologyUpdateRequest(
    val existing_details: List<ExistingDetail?>? = null,
    val new_details: List<NewDetail?>? = null,
    val removed_details: List<RemovedDetail?>? = null
) {
    data class ExistingDetail(
        val details_comments: String? = null,
        val order_priority_uuid: Int? = null,
        val patient_order_uuid: Int? = null,
        val profile_uuid: Any? = null,
        val test_master_uuid: Int? = null,
        val to_location_uuid: Int? = null,
        val type_of_method_uuid: Int? = null,
        val uuid: Int? = null
    )

    data class NewDetail(
        val doctor_uuid: String? = null,
        val encounter_type_uuid: String? = null,
        val encounter_uuid: String? = null,
        val from_department_uuid: String? = null,
        val group_uuid: Int? = null,
        val is_ordered: Boolean? = null,
        val is_profile: Boolean? = null,
        val lab_master_type_uuid: Int? = null,
        val order_priority_uuid: Int? = null,
        val order_request_date: String? = null,
        val order_status_uuid: Int? = null,
        val patient_order_uuid: Int? = null,
        val patient_uuid: String? = null,
        val patient_work_order_by: Int? = null,
        val profile_uuid: Any? = null,
        val test_master_uuid: Int? = null,
        val to_department_uuid: Int? = null,
        val to_location_uuid: Int? = null,
        val to_sub_department_uuid: Int? = null
    )

    data class RemovedDetail(
        val patient_orders_uuid: Int? = null,
        val profile_uuid: Any? = null,
        val test_master_uuid: Int? = null,
        val uuid: Int? = null
    )
}