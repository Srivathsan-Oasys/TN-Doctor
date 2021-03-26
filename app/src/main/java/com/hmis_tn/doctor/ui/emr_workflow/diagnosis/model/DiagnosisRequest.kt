package com.hmis_tn.doctor.ui.emr_workflow.diagnosis.model

data class DiagnosisRequest(
    var facility_uuid: String = "",
    var department_uuid: String = "",
    var patient_uuid: String = "",
    var test_master_type_uuid: Int = 0,
    var encounter_uuid: Int = 0,
    var encounter_type_uuid: Int = 0,
    var consultation_uuid: Int = 0,
    var diagnosis_uuid: String = "",
    var condition_type_uuid: Int = 0,
    var condition_status_uuid: Int = 0,
    var other_diagnosis: String = "",
    var category_uuid: Int = 0,
    var type_uuid: Int = 0,
    var grade_uuid: Int = 0,
    var prescription_uuid: Int = 0,
    var patient_treatment_uuid: Int = 0,
    var comments: String = "",
    var is_snomed: Int = 0,
    var tat_end_time: String = "",
    var tat_start_time: String = "",
    var body_site_uuid: Int = 0
)