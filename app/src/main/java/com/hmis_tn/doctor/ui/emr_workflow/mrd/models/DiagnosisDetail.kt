package com.hmis_tn.doctor.ui.emr_workflow.mrd.models

data class DiagnosisDetail(
    val department_uuid: Int = 0,
    val diagnosis_code: String = "",
    val diagnosis_desc: String = "",
    val diagnosis_name: String = "",
    val diagnosis_type: String = "",
    val diagnosis_uuid: Int = 0,
    val encounter_type_code: String = "",
    val encounter_type_name: String = "",
    val encounter_type_uuid: Int = 0,
    val encounter_uuid: Int = 0,
    val facility_uuid: Int = 0,
    val patient_diagnosis_uuid: Int = 0,
    val patient_uuid: Int = 0,
    val performed_by: Int = 0,
    val performed_date: String = ""
)