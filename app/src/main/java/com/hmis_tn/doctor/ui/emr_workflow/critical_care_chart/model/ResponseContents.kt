package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model

data class ResponseContents(
    var Is_default: Boolean?,
    var color: String?,
    var critical_care_charts: List<CriticalCareChart>?,
    var display_order: Int?,
    var is_active: Boolean?,
    var language: Any?,
    var name: String?,
    var status: Boolean?,
    var uuid: Int?
)