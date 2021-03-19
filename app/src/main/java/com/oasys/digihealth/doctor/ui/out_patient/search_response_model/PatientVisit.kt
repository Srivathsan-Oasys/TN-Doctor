package com.oasys.digihealth.doctor.ui.out_patient.search_response_model

data class PatientVisit(
    var department_uuid: Int? = null,
    var is_last_visit: Boolean? = null,
    var patient_uuid: Int? = null,
    var registered_date: String? = null,
    var session_uuid: Int? = null
)