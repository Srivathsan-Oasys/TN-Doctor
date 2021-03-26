package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model

data class CriticalCareConceptValue(
    var cc_concept_uuid: Int?,
    var concept_value: String?,
    var display_order: Int?,
    var is_active: Boolean?,
    var is_default: Boolean?,
    var status: Boolean?,
    var uuid: Int?,
    var value_from: Int?,
    var value_to: Int?
)