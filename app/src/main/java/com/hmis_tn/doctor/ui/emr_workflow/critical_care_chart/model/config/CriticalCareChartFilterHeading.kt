package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model.config

data class CriticalCareChartFilterHeading(
    var cc_type_code: String? = null,
    var cc_type_id: Int? = null,
    var cc_type_name: String? = null,
    var ecs_is_active: Boolean? = null,
    var emr_history_settings_id: Int? = null,
    var facility_uuid: Int? = null,
    var user_uuid: Int? = null
) {
    var isSelected = false
}