package com.hmis_tn.doctor.ui.emr_workflow.model.EMR_Request

data class Header(
    var consultation_uuid: Int = 0,
    var department_uuid: String = "",
    var doctor_uuid: String = "",
    var encounter_type_uuid: Int = 0,
    var encounter_uuid: Int = 0,
    var facility_uuid: String = "",
    var lab_master_type_uuid: Int = 0,
    var order_status_uuid: Int = 0,
    var order_to_location_uuid: Int = 0,
    var patient_treatment_uuid: Int = 0,
    var patient_uuid: String = "",
    var sub_department_uuid: Int = 0,
    var treatment_plan_uuid: Int = 0,
    var encounter_doctor_uuid: Int = 0,
    var is_active: Boolean = true,
    var order_date: String = "",
    var tat_session_end: String = "",
    var tat_session_start: String = "",
    var status: Boolean = true,
    var application_type_uuid: Int? = null,
    var ward_uuid: Int = 0

)