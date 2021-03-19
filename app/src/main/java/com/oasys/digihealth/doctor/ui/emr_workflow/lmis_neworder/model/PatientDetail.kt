package com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model

data class PatientDetail(
    var aadhaar_number: String? = "",
    var address_line1: String? = "",
    var address_line2: String? = "",
    var address_line3: String? = "",
    var address_line4: String? = "",
    var address_line5: String? = "",
    var airport_name: Any? = Any(),
    var alternate_number: Any? = Any(),
    var attender_contact_number: Any? = Any(),
    var attender_name: Any? = Any(),
    var bill_class: Any? = Any(),
    var block_uuid: Int? = 0,
    var city_uuid: Int? = 0,
    var community_uuid: Int? = 0,
    var complication_uuid: Int? = 0,
    var contact_history: Boolean? = false,
    var contact_history_details: Any? = Any(),
    var country_uuid: Int? = 0,
    var created_by: Int? = 0,
    var created_date: String? = "",
    var date_of_onset: Any? = Any(),
    var death_approved_by: Int? = 0,
    var death_coments: Any? = Any(),
    var death_confirmed_by: Int? = 0,
    var death_place: String? = "",
    var death_updated_by: Int? = 0,
    var death_updated_date: Any? = Any(),
    var district_uuid: Int? = 0,
    var email: String? = "",
    var height: String? = "",
    var ili: Boolean? = false,
    var income_uuid: Int? = 0,
    var is_active: Boolean? = false,
    var is_death_confirmed: Any? = Any(),
    var is_email_communication_preference: Any? = Any(),
    var is_rapid_test: Boolean? = false,
    var is_sms_communication_preference: Any? = Any(),
    var lab_to_facility_uuid: Int? = 0,
    var location_travelled: Any? = Any(),
    var marital_uuid: Int? = 0,
    var mobile: String? = "",
    var modified_by: Int? = 0,
    var modified_date: String? = "",
    var nationality_type_uuid: Int? = 0,
    var no_symptom: Boolean? = false,
    var occupation_uuid: Int? = 0,
    var op_status: Any? = Any(),
    var other_proof_number: Any? = Any(),
    var other_proof_uuid: Int? = 0,
    var out_come_date: Any? = Any(),
    var out_come_type_uuid: Int? = 0,
    var para_1: Any? = Any(),
    var patient_uuid: Int? = 0,
    var photo_path: String? = "",
    var pin_status: Any? = Any(),
    var pincode: String? = "",
    var place_uuid: Int? = 0,
    var quarantine_status_date: Any? = Any(),
    var quarentine_status: Any? = Any(),
    var quarentine_status_type_uuid: Int? = 0,
    var referred_doctor: Any? = Any(),
    var relation_type_uuid: Int? = 0,
    var religion_uuid: Int? = 0,
    var repeat_test: Boolean? = false,
    var repeat_test_date: Any? = Any(),
    var repeat_test_type_uuid: Int? = 0,
    var revision: Boolean? = false,
    var sample_type_uuid: Int? = 0,
    var sari: Boolean? = false,
    var smart_ration_number: String? = "",
    var state_uuid: Int? = 0,
    var symptom_duration: Any? = Any(),
    var symptoms: Any? = Any(),
    var taluk_uuid: Int? = 0,
    var test_location: Any? = Any(),
    var test_result: Boolean? = false,
    var travel_history: Any? = Any(),
    var travel_history_date: Any? = Any(),
    var travel_history_to_date: Any? = Any(),
    var treatment_category_uuid: Int? = 0,
    var treatment_plan_uuid: Int? = 0,
    var underline_medicine_condition_uuid: Int? = 0,
    var underline_medicine_details: Any? = Any(),
    var uuid: Int? = 0,
    var village_uuid: Int? = 0,
    var weight: String? = ""
)