package com.oasys.digihealth.doctor.ui.emr_workflow.history.diagnosis.model

data class HistoryDiagnosisresponseContent(
    val diagnosis_code: String? = "",
    val diagnosis_comments: String? = "",
    val diagnosis_created_date: String? = "",
    val diagnosis_is_snomed: Boolean? = false,
    val diagnosis_modified_date: String? = "",
    val diagnosis_name: String? = "",
    val diagnosis_performed_by: Int? = 0,
    val diagnosis_performed_date: String? = "",
    val diagnosis_uuid: Int? = 0,
    val encounter_type_id: Int? = 0,
    val patient_diagnosis_id: Int? = 0
)