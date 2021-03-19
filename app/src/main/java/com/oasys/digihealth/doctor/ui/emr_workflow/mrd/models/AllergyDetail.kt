package com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models

data class AllergyDetail(
    val allergy_duration: Int = 0,
    val allergy_name: String = "",
    val allergy_severity_code: String = "",
    val allergy_severity_name: String = "",
    val allergy_severity_uuid: Int = 0,
    val allergy_source_code: String = "",
    val allergy_source_name: String = "",
    val allergy_source_uuid: Int = 0,
    val allergy_type_code: String = "",
    val allergy_type_name: String = "",
    val allergy_type_uuid: Int = 0,
    val allergy_uuid: Int = 0,
    val date: String = "",
    val encounter_type_code: String = "",
    val encounter_type_name: String = "",
    val encounter_type_uuid: Int = 0,
    val patient_uuid: Int = 0,
    val periods_code: String = "",
    val periods_name: String = "",
    val symptom: Any = Any(),
    val uuid: Int = 0
)