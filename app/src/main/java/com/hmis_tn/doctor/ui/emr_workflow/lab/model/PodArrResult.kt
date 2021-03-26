package com.hmis_tn.doctor.ui.emr_workflow.lab.model

data class PodArrResult(
    val code: String = "",
    val name: String = "",
    val order_priority_name: String = "",
    val order_priority_uuid: Int = 0,
    val order_to_location: String = "",
    val order_to_location_uuid: Int = 0,
    val test_master_uuid: Int = 0,
    val type: String = "",
    val patient_order_details_uuid: Int = 0,
    val type_uuid: Int = 0,
    val test_method_uuid: Int = 0,
    val patient_order_uuid: Int = 0
)