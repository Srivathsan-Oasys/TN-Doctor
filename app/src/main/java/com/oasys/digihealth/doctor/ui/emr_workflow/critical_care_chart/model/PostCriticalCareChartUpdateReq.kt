package com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model

data class PostCriticalCareChartUpdateReq(
    var headers: HeadersX?,
    var observed_data: List<ObservedDataX>?
)