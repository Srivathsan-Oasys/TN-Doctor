package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model

data class GetCriticalCareChartEncounterResp(
    var code: Int?,
    var message: String?,
    var responseContents: List<ResponseContent>?
)