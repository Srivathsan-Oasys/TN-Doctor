package com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model

data class VentilatorDetails(
    var comments: String?,
    var encounter_type_uuid: Int?,
    var encounter_uuid: Int?,
    var facility_uuid: Int?,
    var patient_uuid: Int?
)