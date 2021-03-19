package com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.request

data class Header(
    var consultation_uuid: Int? = 0,
    var department_uuid: String? = "",
    var doctor_uuid: String? = "",
    var encounter_doctor_uuid: Int? = 0,
    var encounter_type_uuid: Int? = 0,
    var encounter_uuid: Int? = 0,
    var facility_uuid: String? = "",
    var from_facility_name: Any? = Any(),
    var lab_master_type_uuid: Int? = 0,
    var order_status_uuid: Int? = 0,
    var order_to_location_uuid: Int? = 0,
    var patient_treatment_uuid: Int? = 0,
    var patient_uuid: String? = "",
    var sub_department_uuid: Int? = 0,
    var tat_session_end: String? = "",
    var tat_session_start: String? = "",
    var application_type_uuid: Int? = null,
    var ward_uuid: Int? = null,
    var treatment_plan_uuid: Int? = 0
)