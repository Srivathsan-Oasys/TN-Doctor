package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model

data class RadiologyResponseContents(
    val create_details: List<CreateDetail> = listOf(),
    val update_details: List<UpdateDetail> = listOf()
)