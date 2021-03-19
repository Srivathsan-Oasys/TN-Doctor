package com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model

data class CriticalCareConcepts(
    var cc_chart_uuid: Int?,
    var concept_code: String?,
    var concept_name: String?,
    var critical_care_concept_values: List<CriticalCareConceptValue>?,
    var display_order: Int?,
    var is_active: Boolean?,
    var is_default: Boolean?,
    var is_mandatory: Boolean?,
    var is_multiple: Boolean?,
    var status: Boolean?,
    var uuid: Int?,
    var value_type_uuid: Int?
)