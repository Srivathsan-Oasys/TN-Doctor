package com.hmis_tn.doctor.ui.emr_workflow.diagnosis.model

data class DiagnosisInsertRequest(
    var body_site_uuid: Int = 0,
    var category_uuid: Int = 0,
    var comments: String = "",
    var condition_status_uuid: Int = 0,
    var condition_type_uuid: Int = 0,
    var consultation_uuid: Int = 0,
    var department_uuid: String = "",
    var diagnosis_uuid: Int = 0,
    var encounter_type_uuid: EncounterTypeUuid = EncounterTypeUuid(),
    var encounter_uuid: Int = 0,
    var facility_uuid: String = "",
    var grade_uuid: Int = 0,
    var patient_treatment_uuid: Int = 0,
    var patient_uuid: String = "",
    var prescription_uuid: Int = 0,
    var tat_end_time: String = "",
    var tat_start_time: String = "",
    var test_master_type_uuid: Int = 0,
    var type_uuid: Int = 0
)