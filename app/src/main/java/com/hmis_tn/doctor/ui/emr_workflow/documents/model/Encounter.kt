package com.hmis_tn.doctor.ui.emr_workflow.documents.model

data class Encounter(
    val encounter_date: String = "",
    val patient_uuid: Int = 0,
    val uuid: Int = 0
)