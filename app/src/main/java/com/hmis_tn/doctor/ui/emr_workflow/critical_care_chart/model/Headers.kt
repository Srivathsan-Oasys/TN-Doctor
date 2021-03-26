package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model

data class Headers(
    var comments: String?,
    var critical_care_type: Int?,
    var encounter_type_uuid: Int?,
    var encounter_uuid: Int?,
    var facility_uuid: String?,
    var patient_uuid: String?
)