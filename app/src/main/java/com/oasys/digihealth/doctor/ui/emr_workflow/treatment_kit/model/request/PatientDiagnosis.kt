package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.request

data class PatientDiagnosis(
    var body_site_uuid: Int? = 0,
    var category_uuid: Int? = 0,
    var condition_status_uuid: Int? = 0,
    var condition_type_uuid: Int? = 0,
    var consultation_uuid: Int? = 0,
    var department_uuid: String? = "",
    var diagnosis_uuid: Int? = 0,
    var encounter_type_uuid: Int? = 0,
    var encounter_uuid: Int? = 0,
    var facility_uuid: String? = "",
    var grade_uuid: Int? = 0,
    var patient_treatment_uuid: Int? = 0,
    var patient_uuid: String? = "",
    var prescription_uuid: Int? = 0,
    var type_uuid: Int? = 0,
    var treatment_kit_uuid: Int? = 0,
    var test_master_type_uuid: Int? = 0,
    var other_diagnosis: String? = "",
    var is_snomed: Int? = 0,
    var tat_start_time: String? = "",
    var tat_end_time: String? = "",
    var ward_master_uuid: Int? = 0,
    var encounter_doctor_uuid: Int? = 0
)