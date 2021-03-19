package com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.config

data class CriticalCareChartFilterHeadingsResponse(
    var responseContents: List<CriticalCareChartFilterHeading>? = null,
    var code: Int? = null,
    var message: String? = null
)