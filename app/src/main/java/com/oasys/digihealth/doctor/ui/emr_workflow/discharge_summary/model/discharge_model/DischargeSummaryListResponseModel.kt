package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.discharge_model

data class DischargeSummaryListResponseModel(
    val discharge_result: DischargeResult? = null,
    val message: String = "",
    val status: String = "",
    val statusCode: Int = 0
)