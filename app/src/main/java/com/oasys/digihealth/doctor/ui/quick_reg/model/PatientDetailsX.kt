package com.oasys.digihealth.doctor.ui.quick_reg.model

data class PatientDetailsX(
    val aadhaar_number: String? = "",
    val address_line1: String? = "",
    val block_uuid: Int? = 0,
    val country_uuid: Int? = 0,
    val district_uuid: Int? = 0,
    val height: String? = "",
//    val ili: Boolean? = false,
//    val is_rapid_test: Boolean? = false,
    val mobile: String? = "",
    val modified_by: String? = "",
    val modified_date: String? = "",
//    val no_symptom: Boolean? = false,
    val patient_uuid: Int? = 0,
    val pincode: String? = "",
    val revision: Int? = 0,
    val sample_type_uuid: Int? = 0,
//    val sari: Boolean? = false,
    val state_uuid: Int? = 0,
    val weight: String? = ""
)