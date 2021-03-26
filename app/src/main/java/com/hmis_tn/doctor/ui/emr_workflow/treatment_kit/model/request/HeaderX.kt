package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.request

data class HeaderX(
    var department_uuid: String? = "",
    var dispense_status_uuid: Int? = 0,
    var doctor_uuid: String? = "",
    var encounter_type_uuid: Int? = 0,
    var encounter_uuid: Int? = 0,
    var notes: String? = "",
    var patient_uuid: String? = "0",
    var prescription_status_uuid: Int? = 0,
    var store_master_uuid: Int? = 0,
    var treatment_kit_uuid: Int? = 0,
    var encounter_doctor_uuid: Int = 0,
    var injection_room_uuid: Int = 0
)