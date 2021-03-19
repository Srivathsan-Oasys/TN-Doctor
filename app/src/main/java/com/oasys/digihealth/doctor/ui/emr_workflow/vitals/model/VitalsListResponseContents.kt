package com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model


data class VitalsListResponseContents(
    val getVitals: List<TemplateDetail> = listOf()
)