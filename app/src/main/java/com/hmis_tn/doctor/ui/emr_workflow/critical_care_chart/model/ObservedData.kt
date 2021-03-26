package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model

data class ObservedData(
    var cc_chart_uuid: Int?,
    var cc_concept_uuid: Int?,
    var cc_concept_value_uuid: Int?,
    var from_date: String?,
    var observed_value: Any?,
    var to_date: String?
)