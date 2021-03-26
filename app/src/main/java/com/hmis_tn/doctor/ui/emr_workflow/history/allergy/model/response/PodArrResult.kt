package com.hmis_tn.doctor.ui.emr_workflow.history.allergy.model.response

data class PodArrResult(
    val code: String? = "",
    val name: String? = "",
    val order_priority_name: String? = "",
    val order_priority_uuid: Int? = 0,
    val order_to_location: String? = "",
    val order_to_location_uuid: Int? = 0,
    val test_master_uuid: Int? = 0,
    val type: String? = "",
    val type_uuid: Int? = 0
)