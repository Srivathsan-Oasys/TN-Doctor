package com.hmis_tn.doctor.ui.quick_reg.model.reports.response


data class LabConsolidatedReportResponseModel(
    val responseContents: List<LabConsolidatedReportResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)