package com.oasys.digihealth.doctor.ui.covid.addpatientrequest

import com.oasys.digihealth.doctor.ui.covid.addpatientrequest.*

data class AddPatientDetailsRequest(
    var aadhaar_number: String? = "",
    var address_line1: String? = "",
    var address_line2: String? = "",
    var age: Int? = 0,
    var airport_name: String? = "",
    var alternate_number: String? = "",
    var contact_history: String? = "",
    var contact_history_details: String? = "",
    var country_uuid: Int? = 0,
    var covid_patient_details: CovidPatientDetails? = CovidPatientDetails(),
    var date_of_onset: String? = "",
    var department_uuid: String? = "",
    var district_uuid: Int? = 0,
    var email: String? = "",
    var fingerPrintData: FingerPrintData? = FingerPrintData(),
    var first_name: String? = "",
    var gender_uuid: Int? = 0,
    var isWeb: Boolean? = false,
    var is_adult: Int? = 0,
    var is_dob_auto_calculate: Int? = 0,
    var location_travelled: String? = "",
    var mobile: String? = "",
    var nationality_type_uuid: Int? = 0,
    var out_come_date: String? = "",
    var out_come_type_uuid: String? = "",
    var patient_condition_details: ArrayList<PatientConditionDetail?>? = ArrayList(),
    var patient_specimen_details: ArrayList<PatientSpecimenDetail?>? = ArrayList(),
    var patient_symptoms: ArrayList<PatientSymptom?>? = ArrayList(),
    var period_uuid: Int? = 0,
    var pincode: String? = "",
    var quarantine_status_date: String? = "",
    var quarentine_status: String? = "",
    var quarentine_status_type_uuid: String? = "",
    var referred_doctor: String? = "",
    var registred_facility_uuid: String? = "",
    var repeat_test: String? = "",
    var repeat_test_date: String? = "",
    var repeat_test_type_uuid: String? = "",
    var session_uuid: Int? = 1,
    var state_uuid: Int? = 0,
    var symptom_duration: String? = "",
    var symptoms: Boolean? = false,
    var taluk_uuid: Int? = 0,
    var test_location: String? = "",
    var title_uuid: Int? = 0,
    var travel_history: String? = "",
    var travel_history_date: String? = "",
    var travel_history_to_date: String? = "",
    var underline_medicine_condition_uuid: String? = "",
    var underline_medicine_details: String? = "",
    var village_uuid: Int? = 0
)