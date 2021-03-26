package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model


data class Prescription(
    val prescription_details: List<PrescriptionDetail> = listOf(),
    val prescription_headers: PrescriptionHeaders = PrescriptionHeaders()
)