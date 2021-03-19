package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.previous_model

data class DsDetails(
    val admission_id: Int = 0,
    val data_template: String = "",
    val dctor_name: String = "",
    val department_uuid: Int = 0,
    val discharge_id: Int = 0,
    val doctor_id: Int = 0,
    val encounter_id: Int = 0,
    val facility_id: Int = 0,
    val generated_by_id: Int = 0,
    val generated_date: String = "",
    val note_template_id: Int = 0,
    val nurse_name: String = "",
    val patient_id: Int = 0
)