package com.hmis_tn.doctor.ui.emr_workflow.history.immunization.model

data class CreateImmunizationresponseContents(
    val administered_date: String? = "",
    val comments: String? = "",
    val consultation_uuid: Int? = 0,
    val created_by: String? = "",
    val created_date: String? = "",
    val display_order: Int? = 0,
    val duration: String? = "",
    val encounter_uuid: Int? = 0,
    val facility_uuid: Int? = 0,
    val immunization_date: String? = "",
    val immunization_dosage_uuid: Int? = 0,
    val immunization_period_uuid: Int? = 0,
    val immunization_schedule_flag_uuid: Int? = 0,
    val immunization_schedule_status_uuid: Int? = 0,
    val immunization_schedule_uuid: String? = "",
    val immunization_uuid: String? = "",
    val is_active: Boolean? = false,
    val modified_by: String? = "",
    val modified_date: String? = "",
    val patient_uuid: String? = "",
    val revision: Int? = 0,
    val route_uuid: Int? = 0,
    val schedule_uuid: Int? = 0,
    val status: Boolean? = false
)