package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model

import com.google.gson.annotations.SerializedName

data class EncounterResponseContent(
    val admission_request_uuid: Int = 0,
    val admission_uuid: Int = 0,
    val appointment_uuid: Int = 0,
    val created_by: Int = 0,
    val created_date: String = "",
    val department_uuid: Int = 0,
    @SerializedName("discharge_date")
    val discharge_date: String? = null,
    val discharge_type_uuid: Int = 0,
    val encounter_date: String = "",
    val encounter_doctors: List<EncounterDoctor> = listOf(),
    val encounter_identifier: Int = 0,
    val encounter_priority_uuid: Int = 0,
    val encounter_status_uuid: Int = 0,
    val encounter_type_uuid: Int = 0,
    val facility_uuid: Int = 0,
    val is_active: Boolean = false,
    val is_active_encounter: Boolean = false,
    val is_followup: Boolean = false,
    val is_group_casuality: Boolean = false,
    val is_mrd_request: Boolean = false,
    val modified_by: Int = 0,
    val modified_date: String = "",
    val patient_uuid: Int = 0,
    val revision: Boolean = false,
    val status: Boolean = false,
    val uuid: Int = 0
)