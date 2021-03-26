package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model

data class ObservedDataX(
    var cc_chart_uuid: Int?,
    var cc_concept_uuid: Int?,
    var cc_concept_value_uuid: Int?,
    var observed_value: Any?,
    var to_date: String?,
    var uuid: Any?
)