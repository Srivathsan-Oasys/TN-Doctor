package com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models

data class VwPatientInfo(
    val age: String = "",
    val ageperiod: String = "",
    val first_name: String = "",
    val gender_uuid: Int = 0,
    val last_name: String = "",
    val middle_name: String = "",
    val mobile: String = "",
    val pattitle: String = "",
    val uhid: String = "",
    val uuid: Int = 0
)