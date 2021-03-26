package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model

data class DischargSaveeRequest(
    var admission_request_uuid: String = "",
    var admission_status_uuid: Int = 0,
    var admission_uuid: String = "",
    var death_type_uuid: Int = 0,
    var discharge_type_uuid: String = "",
    var encounter_type_uuid: Int = 0,
    var encounter_uuid: String = "",
    var patient_uuid: String = ""
)