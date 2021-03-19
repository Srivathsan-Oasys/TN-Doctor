package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.discharge_model

data class Diagnosis(
    val diagnosis_details: List<DiagnosisDetail> = listOf(),
    val diagnosis_headers: DiagnosisHeaders = DiagnosisHeaders()
)