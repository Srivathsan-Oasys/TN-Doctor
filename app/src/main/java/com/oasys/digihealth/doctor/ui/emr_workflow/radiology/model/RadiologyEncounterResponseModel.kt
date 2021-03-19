package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model

data class RadiologyEncounterResponseModel(
    val code: Int = 0,
    val message: String = "",
    val responseContents: List<RadiologyEncounterResponseContent> = listOf()
)