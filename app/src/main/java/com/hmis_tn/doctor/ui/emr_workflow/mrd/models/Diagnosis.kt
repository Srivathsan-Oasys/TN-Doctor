package com.hmis_tn.doctor.ui.emr_workflow.mrd.models

data class Diagnosis(
    val diagnosis_details: List<DiagnosisDetail> = listOf()
)