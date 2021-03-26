package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model

data class Radiology(
    val radiology_details: List<RadiologyDetail> = listOf(),
    val radiology_headers: RadiologyHeaders = RadiologyHeaders()
)