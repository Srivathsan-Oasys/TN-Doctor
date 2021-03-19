package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model

data class RadilogyDetail(
    val comments: String? = "",
    val details_uuid: Int? = 0,
    val encounter_type_uuid: Int? = 0,
    val lab_code: String? = "",
    val lab_name: String? = "",
    val location_code: String? = "",
    val location_name: String? = "",
    val order_id: Int? = 0,
    val order_status_code: String? = "",
    val order_status_name: String? = "",
    val order_status_uuid: Int? = 0,
    val priority_code: String? = "",
    val priority_uuid: Int? = 0,
    val prority_name: String? = "",
    val test_master_uuid: Int? = 0,
    val to_location_uuid: Int? = 0,
    val uuid: Int? = 0,
    val profile_master_uuid: Int? = 0,
    val profile_master_name: String? = "",
    val profile_master_code: String? = ""
)