package com.hmis_tn.doctor.ui.out_patient.search_response_model

data class ResponseContent(
    var age: Int? = null,
    var application_identifier: Any? = null,
    var application_type_uuid: Int? = null,
    var application_uuid: Any? = null,
    var blood_group_uuid: Int? = null,
    var created_by: Int? = null,
    var created_date: String? = null,
    var dob: String? = null,
    var facility_details: FacilityDetails? = null,
    var first_name: String? = null,
    var gender_details: GenderDetails? = null,
    var gender_uuid: Int? = null,
    var is_active: Boolean? = null,
    var is_adult: Boolean? = null,
    var is_dob_auto_calculate: Boolean? = null,
    var last_name: Any? = null,
    var middle_name: Any? = null,
    var modified_by: Int? = null,
    var modified_date: String? = null,
    var old_pin: Any? = null,
    var patient_detail: PatientDetail? = null,
    var patient_type_uuid: Int? = null,
    var patient_visits: List<PatientVisit?>? = null,
    var _uuid: Int? = null,
    var registered_date: String? = null,
    var registred_facility_uuid: Int? = null,

    var title_uuid: Int? = null,
    var uhid: String? = null,
    var uuid: Int? = null
)