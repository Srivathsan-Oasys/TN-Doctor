package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.nurse_desk.save_model

data class Headers(
    var admission_request_uuid: Int = 0,
    var admission_status_uuid: Int = 0,
    var admission_uuid: Int = 0,
    var comments: String = "",
    var reason: String? = null,
    var data_template: String = "",
    var death_place: String = "",
    var death_date: String = "",
    var death_type_uuid: Int = 0,
    var department_uuid: String = "",
    var discharge_date: String = "",
    var discharge_type_uuid: String = "",
    var encounter_type_uuid: Int = 0,
    var encounter_uuid: Int = 0,
    var facility_uuid: String = "",
    var generated_by: Int = 0,
    var generated_date: String = "",
    var is_alive: Int = 1,
    var note_template_uuid: Int = 0,
    var patient_uuid: Int = 0,
    var note_template_is_default: Int = 0,
    var doctor_uuid: String = ""
)