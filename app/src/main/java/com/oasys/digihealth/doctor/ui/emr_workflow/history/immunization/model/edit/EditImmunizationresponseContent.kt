package com.oasys.digihealth.doctor.ui.emr_workflow.history.immunization.model.edit

data class EditImmunizationresponseContent(
    val administered_date: String? = "",
    val comments: String? = "",
    val consultation_uuid: Int? = 0,
    val created_by: Int? = 0,
    val created_date: String? = "",
    val display_order: String? = "",
    val duration: Int? = 0,
    val encounter_uuid: Int? = 0,
    val facility_uuid: Int? = 0,
    val immunization_date: String? = "",
    val immunization_dosage_uuid: Int? = 0,
    val immunization_schedule_flag_uuid: Int? = 0,
    val immunization_schedule_status_uuid: Int? = 0,
    val immunization_schedule_uuid: Int? = 0,
    val immunization_uuid: Int? = 0,
    val is_active: Boolean? = false,
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    val patient_uuid: Int? = 0,
    val revision: Int? = 0,
    val route_uuid: Int? = 0,
    val schedule_uuid: Int? = 0,
    val status: Boolean? = false,
    val uuid: Int? = 0
)