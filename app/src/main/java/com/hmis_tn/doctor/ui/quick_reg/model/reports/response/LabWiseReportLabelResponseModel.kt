package com.hmis_tn.doctor.ui.quick_reg.model.reports.response


data class LabWiseReportLabelResponseModel(
    val responseContents: List<LabWiseReportLabelResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0
)