package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model

data class TreatmentKitPrevResponsModel(
    val code: Int = 0,
    val message: String = "",
    val responseContents: List<TreatmentKitResponsResponseContent> = listOf()
)