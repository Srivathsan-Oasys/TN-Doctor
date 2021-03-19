package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.request

data class PatientTreatment(
    var comments: String? = "",
    var consultation_uuid: Int? = 0,
    var department_uuid: String? = "",
    var encounter_type_uuid: Int? = 0,
    var encounter_uuid: Int? = 0,
    var facility_uuid: String? = "",
    var patient_uuid: String? = "",
    var treatment_kit_uuid: Int? = 0,
    var treatment_status_uuid: Int? = 0
)