package com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model

data class Data(
    var cc_chart_uuid: Int?,
    var cc_concept_uuid: Int?,
    var cc_concept_value_uuid: Int?,
    var comments: String?,
    var created_by: String?,
    var created_date: String?,
    var critical_care_type: Int?,
    var encounter_type_uuid: Int?,
    var encounter_uuid: Int?,
    var facility_uuid: String?,
    var from_date: String?,
    var is_active: Int?,
    var modified_by: Int?,
    var modified_date: String?,
    var observed_value: Any?,
    var patient_uuid: String?,
    var revision: Int?,
    var status: Int?,
    var to_date: String?
)