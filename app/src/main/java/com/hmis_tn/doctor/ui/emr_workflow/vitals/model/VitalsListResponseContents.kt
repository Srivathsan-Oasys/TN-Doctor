package com.hmis_tn.doctor.ui.emr_workflow.vitals.model


data class VitalsListResponseContents(
    val getVitals: List<TemplateDetail> = listOf()
)