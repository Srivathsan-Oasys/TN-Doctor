package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model

data class PostCriticalCareChartCreateReq(
    var headers: Headers?,
    var observed_data: List<ObservedData>?
)