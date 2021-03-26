package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.search

data class LabDetail(
    val lab_code: String?,
    val lab_description: String?,
    val lab_id: Int?,
    val lab_name: String?,
    val order_priority_uuid: Int?,
    val order_to_location_uuid: Int?,
    val test_type: String?
)