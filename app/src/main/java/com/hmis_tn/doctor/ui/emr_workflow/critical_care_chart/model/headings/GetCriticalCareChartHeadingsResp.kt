package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.headings

data class GetCriticalCareChartHeadingsResp(
    val code: Int?,
    val message: String?,
    val responseContents: List<ResponseContent>?,
    val totalRecords: Int?
)