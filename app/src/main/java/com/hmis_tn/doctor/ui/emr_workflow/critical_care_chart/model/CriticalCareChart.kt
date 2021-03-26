package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model

data class CriticalCareChart(
    var code: String?,
    var comments: String?,
    var critical_care_concepts: CriticalCareConcepts?,
    var critical_care_type_uuid: Int?,
    var critical_care_uom_uuid: Int?,
    var description: Any?,
    var is_active: Boolean?,
    var loinc_code_master_uuid: Int?,
    var mnemonic_code_master_uuid: Int?,
    var name: String?,
    var status: Boolean?,
    var uuid: Int?
) {
    var answer: String? = null
}