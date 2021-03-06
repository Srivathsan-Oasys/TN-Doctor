package com.hmis_tn.doctor.ui.out_patient.search_response_model

data class ResponseContentOldPatient(
    var age: Int? = 0,
    var application_identifier: Any? = Any(),
    var application_type_uuid: Int? = 0,
    var application_uuid: Any? = Any(),
    var blood_group_uuid: Int? = 0,
    var created_by: Int? = 0,
    var created_date: String? = "",
    var dob: String? = "",
    var first_name: String? = "",
    var gender_uuid: Int? = 0,
    var is_active: Boolean? = false,
    var is_adult: Boolean? = false,
    var is_dob_auto_calculate: Boolean? = false,
    var last_name: String? = "",
    var middle_name: String? = "",
    var modified_by: Int? = 0,
    var modified_date: String? = "",
    var old_pin: Long? = 0,
    var package_uuid: Int? = 0,
    var para_1: Any? = Any(),
    var patient_detail: PatientDetailX? = PatientDetailX(),
    var patient_type_uuid: Int? = 0,
    var patient_visits: List<PatientVisitX?>? = listOf(),
    var period_uuid: Int? = 0,
    var professional_title_uuid: Int? = 0,
    var registered_date: String? = "",
    var registred_facility_uuid: Int? = 0,
    var revision: Boolean? = false,
    var suffix_uuid: Int? = 0,
    var tat_end_time: Any? = Any(),
    var tat_start_time: Any? = Any(),
    var title_uuid: Int? = 0,
    var uhid: String? = "",
    var uuid: Int? = 0,
    var gender: String = "",
    var crt_dt: String = ""
)