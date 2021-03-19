package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.response

data class TreatmentKitInvestigation(
    val created_by: String? = "",
    val created_date: String? = "",
    val investigation_description: String? = "",
    val is_active: Int? = 0,
    val modified_by: String? = "",
    val modified_date: String? = "",
    val order_priority_uuid: String? = "",
    val order_to_location_uuid: String? = "",
    val profile_master_uuid: Int? = 0,
    val revision: Int? = 0,
    val status: Int? = 0,
    val test_master_uuid: String? = "",
    val treatment_kit_uuid: Int? = 0,
    val user_uuid: String? = ""
)