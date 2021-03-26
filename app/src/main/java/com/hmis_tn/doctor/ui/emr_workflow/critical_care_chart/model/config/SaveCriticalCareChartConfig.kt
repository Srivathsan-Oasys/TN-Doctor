package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.config

data class SaveCriticalCareChartConfig(
    var critical_care_type_uuid: Int? = null,
    var department_uuid: String? = null,
    var facility_uuid: String? = null,
    var role_uuid: String? = "1910"
)