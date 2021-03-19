package com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model

data class HeadersX(
    var comments: String?,
    var critical_care_type: Int?,
    var encounter_type_uuid: Int?,
    var encounter_uuid: Int?,
    var facility_uuid: String?,
    var patient_uuid: String?
)