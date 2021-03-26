package com.hmis_tn.doctor.ui.emr_workflow.diet.model.request

data class DietOrderrequestHeaders(
    var department_uuid: String = "",
    var encounter_doctor_uuid: Int = 0,
    var encounter_type_uuid: String = "",
    var encounter_uuid: Int = 0,
    var facility_uuid: String = "",
    var is_active: Boolean = true,
    var order_date: String = "",
    var patient_uuid: String = "",
    var status: Boolean = true,
    var ward_uuid: Int = 0
)