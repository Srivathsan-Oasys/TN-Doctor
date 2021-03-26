package com.hmis_tn.doctor.ui.emr_workflow.diagnosis.model

data class PreDiagnosisResponseContent(
    val diagnosis: Diagnosis = Diagnosis(),
    val diagnosis_uuid: String = "0",
    val encounter_type_uuid: Int = 0,
    val is_snomed: Boolean = false,
    val other_diagnosis: String = "",
    val patient_uuid: Int = 0,
    val uuid: Int = 0,
    val created_date: String = ""
)