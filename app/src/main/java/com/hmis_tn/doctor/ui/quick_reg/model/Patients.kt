package com.hmis_tn.doctor.ui.quick_reg.model

data class Patients(
    val age: Int? = 0,
    val applicationType: String? = "",
    val application_type_uuid: Int? = 0,
    val blood_group_uuid: Int? = 0,
    val created_by: String? = "",
    val created_date: String? = "",
    val department_uuid: String? = "",
    val dob: String? = "",
    val first_name: String? = "",
    val gender_uuid: Int? = 0,
    val is_active: Int? = 0,
    val is_adult: Boolean? = false,
    val is_dob_auto_calculate: Int? = 0,
    val is_mlc: Int? = 0,
    val last_name: String? = "",
    val middle_name: String? = "",
    val modified_by: String? = "",
    val modified_date: String? = "",
    val old_pin: Any? = Any(),
    val patientType: String? = "",
    val patient_type_uuid: Int? = 0,
    val period_uuid: Int? = 0,
    val professional_title_uuid: Int? = 0,
    val registered_date: String? = "",
    val registred_facility_uuid: String? = "",
    val revision: Int? = 0,
    val session_uuid: Int? = 0,
    val suffix_uuid: Int? = 0,
    val title_uuid: Int? = 0,
    val uhid: String? = ""
)