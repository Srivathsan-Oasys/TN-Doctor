package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model

data class DischrageMedication(
    val discharge_medication_details: List<DischargeMedicationDetail> = listOf(),
    val discharge_medication_headers: DischargeMedicationHeaders = DischargeMedicationHeaders()
)