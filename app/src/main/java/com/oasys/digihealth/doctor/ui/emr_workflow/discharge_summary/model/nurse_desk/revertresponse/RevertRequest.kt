package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.nurse_desk.revertresponse

data class RevertRequest(
    var admission_uuid: Int? = null,
    var encounter_type_uuid: Int? = null,
    var encounter_uuid: Int? = null,
    var facility_uuid: String? = null,
    var patient_uuid: Int? = null
)