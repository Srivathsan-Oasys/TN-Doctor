package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.request

data class HeaderXXX(
    var consultation_uuid: Int? = 0,
    var department_uuid: String? = "",
    var doctor_uuid: String? = "",
    var encounter_type_uuid: Int? = 0,
    var encounter_uuid: Int? = 0,
    var lab_master_type_uuid: Int? = 0,
    var order_to_location_uuid: Int? = 0,
    var patient_treatment_uuid: Int? = 0,
    var patient_uuid: String? = "0",
    var sub_department_uuid: Int? = 0,
    var treatment_kit_uuid: Int? = 0,
    var treatment_plan_uuid: Int? = 0
)
