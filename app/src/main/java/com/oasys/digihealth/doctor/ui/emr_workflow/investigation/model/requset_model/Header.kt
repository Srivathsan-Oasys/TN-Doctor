package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model.requset_model

data class Header(
    var consultation_uuid: Int = 0,
    var department_uuid: String = "",
    var doctor_uuid: String = "",
    var encounter_doctor_uuid: String = "",
    var encounter_type_uuid: String = "",
    var encounter_uuid: String = "",
    var facility_uuid: String = "",
    var lab_master_type_uuid: Int = 0,
    var order_status_uuid: Int = 0,
    var order_to_location_uuid: Int = 0,
    var patient_treatment_uuid: Int = 0,
    var patient_uuid: String = "",
    var sub_department_uuid: Int = 0,
    var tat_session_end: String = "",
    var tat_session_start: String = "",
    var treatment_plan_uuid: Int = 0,
    val ward_uuid: Int = 0
)