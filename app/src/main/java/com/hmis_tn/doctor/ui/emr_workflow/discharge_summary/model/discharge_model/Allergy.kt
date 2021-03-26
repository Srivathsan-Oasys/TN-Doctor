package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model

data class Allergy(
    val allergy_details: List<AllergyDetail> = listOf(),
    val allergy_headers: AllergyHeaders = AllergyHeaders()
)