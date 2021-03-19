package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.modify

data class HeaderXX(
    var department_uuid: String? = "",
    var dispense_status_uuid: Int? = 0,
    var doctor_uuid: String? = "",
    var encounter_doctor_uuid: String? = "",
    var encounter_type_uuid: String? = "",
    var encounter_uuid: String? = "",
    var injection_room_uuid: Int? = 0,
    var notes: String? = "",
    var patient_uuid: String? = "",
    var prescription_status_uuid: Int? = 0,
    var store_master_uuid: Any? = Any(),
    var treatment_kit_uuid: Int? = 0
)