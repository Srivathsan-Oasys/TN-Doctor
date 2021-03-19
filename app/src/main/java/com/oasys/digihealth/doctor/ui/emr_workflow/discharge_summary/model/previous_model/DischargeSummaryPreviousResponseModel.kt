package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.previous_model


data class DischargeSummaryPreviousResponseModel(
    val req: String = "",
    val responseContents: DischargeSummaryPreviousResponseContents = DischargeSummaryPreviousResponseContents(),
    val statusCode: Int = 0
)