package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.discharge_model

import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.InvestigationDetails

data class Investigation(
    val investigation_details: List<InvestigationDetails> = listOf(),
    val investigation_headers: InvestigationHeaders = InvestigationHeaders()
)