package com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response

data class CreateEncounterResponseModel(
    val code: Int? = null,
    val message: String? = null,
    val responseContents: CreateEncounterResponseContents? = null
)