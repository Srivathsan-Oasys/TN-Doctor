package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.search

data class InvestigationDetails(
    val order_priority_uuid: Int?,
    val order_to_location_uuid: Int?,
    val investigation_code: String?,
    val investigation_description: String?,
    val investigation_id: Int?,
    val investigation_name: String?,
    val test_type: String?
)
