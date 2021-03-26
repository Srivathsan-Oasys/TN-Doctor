package com.hmis_tn.doctor.ui.out_patient.search_response_model

data class MypatientResponseContent(
    val d_uuid: Int = 0,
    val department_name: String = "",
    val ec_doctor_uuid: Int = 0,
    val ec_encounter_uuid: Int = 0,
    val ec_performed_date: String = "",
    val ec_uuid: Int = 0,
    val g_name: String = "",
    val pa_age: Int = 0,
    val pa_first_name: String = "",
    val pa_last_name: String = "",
    val pa_middle_name: String = "",
    val pa_pin: String = "",
    val patient_uuid: Int = 0,
    val pd_mobile: String = "",
    val t_name: String = ""
)