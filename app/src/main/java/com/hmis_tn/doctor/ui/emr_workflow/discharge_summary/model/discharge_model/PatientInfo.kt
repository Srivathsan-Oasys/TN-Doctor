package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model

data class PatientInfo(
    val age: Int = 0,
    val patient_age_period: String = "",
    val first_name: String = "",
    val gender: String = "",
    val last_name: String = "",
    val middle_name: String = "",
    val mobile: String = "",
    val title_name: String = "",
    val uhid: String = "",
    val uuid: Int = 0
)