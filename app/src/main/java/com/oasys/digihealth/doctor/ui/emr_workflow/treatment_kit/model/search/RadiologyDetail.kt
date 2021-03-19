package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.search

data class RadiologyDetail(
    val order_priority_uuid: Int?,
    val order_to_location_uuid: Int?,
    val radiology_code: String?,
    val radiology_description: String?,
    val radiology_id: Int?,
    val radiology_name: String?,
    val test_type: String?
)