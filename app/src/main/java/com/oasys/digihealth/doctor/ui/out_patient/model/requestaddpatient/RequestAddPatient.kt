package com.oasys.digihealth.doctor.ui.out_patient.model.requestaddpatient

data class RequestAddPatient(
    var age: Int? = 0,
    var country_uuid: Int? = 0,
    var created_date: String? = "",
    var department_uuid: String? = "",
    var district_uuid: Int? = 0,
    var dob: String? = "",
    var first_name: String? = "",
    var gender_uuid: Int? = 0,
    var isEmrWeb: Boolean? = false,
    var is_adult: Int? = 0,
    var is_dob_auto_calculate: Int? = 0,
    var last_name: String? = "",
    var mobile: String? = "",
    var old_pin: Long? = 0,
    var period_uuid: Int? = 0,
    var registred_facility_uuid: String? = "",
    var saveExists: Boolean? = false,
    var session_uuid: Int? = 0,
    var speciality_department_uuid: Int? = 0,
    var state_uuid: Int? = 0,
    var tat_end_time: String? = "",
    var tat_start_time: String? = "",
    var title_uuid: Int? = 0,
    var unit_uuid: String? = ""
)