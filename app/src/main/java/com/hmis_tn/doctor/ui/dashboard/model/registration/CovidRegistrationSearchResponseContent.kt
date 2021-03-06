package com.hmis_tn.doctor.ui.dashboard.model.registration

data class CovidRegistrationSearchResponseContent(
    val age: Int = 0,
    val application_identifier: Int = 0,
    val application_type_uuid: Int = 0,
    val application_uuid: Int = 0,
    val blood_group_uuid: Int = 0,
    val created_by: Int = 0,
    val created_date: String = "",
    val dob: String = "",
    val first_name: String = "",
    val gender_uuid: Int = 0,
    val is_active: Boolean = false,
    val is_adult: Boolean = false,
    val is_dob_auto_calculate: Boolean = false,
    val last_name: String = "",
    val middle_name: String = "",
    val modified_by: Int = 0,
    val modified_date: String = "",
    val old_pin: String = "",
    val patient_detail: PatientDetail = PatientDetail(),
    val patient_type_uuid: Int = 0,
    val patient_visits: ArrayList<PatientVisit> = ArrayList(),
    val period_uuid: Int = 0,
    val professional_title_uuid: Int = 0,
    val registered_date: String = "",
    val registred_facility_uuid: Int = 0,
    val revision: Boolean = false,
    val suffix_uuid: Int = 0,
    val title_uuid: Int = 0,
    val uhid: String = "",
    val uuid: Int = 0
)