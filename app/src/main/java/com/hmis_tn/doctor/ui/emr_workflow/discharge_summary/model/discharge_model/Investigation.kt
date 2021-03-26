package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model

import com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.InvestigationDetails

data class Investigation(
    val investigation_details: List<InvestigationDetails> = listOf(),
    val investigation_headers: InvestigationHeaders = InvestigationHeaders()
)