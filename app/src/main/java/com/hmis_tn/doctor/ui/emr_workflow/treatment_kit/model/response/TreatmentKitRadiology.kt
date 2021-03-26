package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response

data class TreatmentKitRadiology(
    val created_by: String? = "",
    val created_date: String? = "",
    val is_active: Int? = 0,
    val modified_by: String? = "",
    val modified_date: String? = "",
    val order_priority_uuid: String? = "",
    val order_to_location_uuid: String? = "",
    val profile_master_uuid: String? = "",
    val radiology_description: String? = "",
    val revision: Int? = 0,
    val status: Int? = 0,
    val test_master_uuid: Int? = 0,
    val treatment_kit_uuid: Int? = 0,
    val user_uuid: String? = ""
)