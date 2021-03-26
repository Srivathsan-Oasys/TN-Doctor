package com.hmis_tn.doctor.ui.quick_reg.model.reports.response


data class LabConsolidatedReportLabelResponseModel(
    val responseContents: List<LabConsolidatedReportLabelResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0
)