package com.hmis_tn.doctor.ui.emr_workflow.mrd.models

data class Prescription(
    val prescription_details: List<PrescriptionDetail> = listOf()
)