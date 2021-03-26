package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model

data class Vitals(
    val vital_details: List<VitalDetail> = listOf(),
    val vital_headers: VitalHeaders = VitalHeaders()
)