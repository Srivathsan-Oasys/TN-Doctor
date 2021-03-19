package com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models

data class PatientInfo(
    val age: Int = 0,
    val first_name: String = "",
    val gender: String = "",
    val last_name: String = "",
    val middle_name: String = "",
    val mobile: String = "",
    val uhid: String = "",
    val uuid: Int = 0
)