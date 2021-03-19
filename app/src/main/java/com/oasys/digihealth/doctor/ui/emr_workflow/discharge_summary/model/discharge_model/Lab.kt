package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.discharge_model


data class Lab(
    val lab_details: List<LabDetail> = listOf(),
    val lab_headers: LabHeaders = LabHeaders()
)