package com.oasys.digihealth.doctor.ui.quick_reg.model.request

data class Header(
    var assign_to_location_uuid: Int = 0,
    var consultation_uuid: Int = 0,
    var department_uuid: String = "",
    var doctor_uuid: String = "",
    var encounter_doctor_uuid: Int = 0,
    var encounter_type_uuid: Int = 0,
    var encounter_uuid: Int = 0,
    var facility_uuid: String = "",
    var from_facility_name: String ="",
    var is_auto_accept: Int = 0,
    var lab_master_type_uuid: Int = 0,
    var order_status_uuid: Int = 0,
    var order_to_location_uuid: Int = 0,
    var patient_treatment_uuid: Int = 0,
    var patient_uuid: Int = 0,
    var sub_department_uuid: Int = 0,
    var tat_session_end: String = "",
    var tat_session_start: String = "",
    var to_facility_uuid: Int = 0,
    var treatment_plan_uuid: Int = 0
)