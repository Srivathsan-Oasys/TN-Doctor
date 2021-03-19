package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.modify

data class NewDiagnosi(
    var body_site_uuid: Int? = 0,
    var category_uuid: Int? = 0,
    var condition_status_uuid: Int? = 0,
    var condition_type_uuid: Int? = 0,
    var consultation_uuid: Int? = 0,
    var department_uuid: String? = "",
    var diagnosis_uuid: Int? = 0,
    var encounter_doctor_uuid: String? = "",
    var encounter_type_uuid: String? = "",
    var encounter_uuid: String? = "",
    var facility_uuid: String? = "",
    var grade_uuid: Int? = 0,
    var patient_treatment_uuid: Int? = 0,
    var patient_uuid: String? = "",
    var prescription_uuid: Int? = 0,
    var treatment_kit_uuid: Int? = 0,
    var type_uuid: Int? = 0
)