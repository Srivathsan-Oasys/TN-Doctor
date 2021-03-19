package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model

data class PodArrResultX(
    val code: String = "",
    val details_comments: Any = Any(),
    val name: String = "",
    val order_priority_name: String = "",
    val order_priority_uuid: Int = 0,
    val order_status_code: String = "",
    val order_status_name: String = "",
    val order_status_uuid: Int = 0,
    val order_to_location: String = "",
    val order_to_location_uuid: Int = 0,
    val patient_order_details_uuid: Int = 0,
    val patient_order_uuid: Int = 0,
    val profile_code: Any = Any(),
    val profile_master_uuid: Any = Any(),
    val profile_name: Any = Any(),
    val sample_type_uuid: Any = Any(),
    val test_master_uuid: Int = 0,
    val test_method_uuid: Any = Any(),
    val type: String = "",
    val type_uuid: Int = 0
)