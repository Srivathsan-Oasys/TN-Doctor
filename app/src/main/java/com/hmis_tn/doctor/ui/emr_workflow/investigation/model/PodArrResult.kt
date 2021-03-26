package com.hmis_tn.doctor.ui.emr_workflow.investigation.model

data class PodArrResult(
    val code: String = "",
    val name: String = "",
    val order_priority_name: Any = Any(),
    val order_priority_uuid: Int = 0,
    val order_status_code: String = "",
    val comments: String = "",
    val order_status_name: String = "",
    val order_status_uuid: Int = 0,
    val order_to_location: Any = Any(),
    val order_to_location_uuid: Int = 0,
    val patient_order_details_uuid: Int = 0,
    val patient_order_uuid: Int = 0,
    val test_master_uuid: Int = 0,
    val type: Any = Any(),
    val type_uuid: Any = Any()
)