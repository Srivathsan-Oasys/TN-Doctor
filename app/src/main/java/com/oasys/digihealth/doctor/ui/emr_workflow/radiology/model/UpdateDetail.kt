package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model

data class UpdateDetail(
    val created_date: String = "",
    val details_comments: String = "",
    val is_active: Boolean = false,
    val is_confidential: Boolean = false,
    val is_visible_from_facility: Boolean = false,
    val is_visible_to_facility: Boolean = false,
    val modified_date: String = "",
    val order_priority_uuid: Int = 0,
    val patient_order_uuid: Int = 0,
    val status: Boolean = false,
    val to_location_uuid: Int = 0,
    val uuid: Int = 0,
    val vendor_test_uuid: Int = 0
)