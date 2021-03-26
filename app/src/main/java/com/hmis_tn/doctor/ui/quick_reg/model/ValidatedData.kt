package com.hmis_tn.doctor.ui.quick_reg.model

data class ValidatedData(
    val aadhaar_number: String? = "",
    val address_line1: String? = "",
    val age: Int? = 0,
    val block_uuid: Int? = 0,
    val blood_group_uuid: Int? = 0,
    val community_uuid: Int? = 0,
    val country_uuid: Int? = 0,
    val department_uuid: String? = "",
    val district_uuid: Int? = 0,
    val dob: String? = "",
    val first_name: String? = "",
    val gender_uuid: Int? = 0,
    val height: String? = "",
    val ili: Boolean? = false,
    val isIp: Int? = 0,
    val isMobileApi: Int? = 0,
    val isWeb: Boolean? = false,
    val is_active: Int? = 0,
    val is_adult: Int? = 0,
    val is_dob_auto_calculate: Int? = 0,
    val is_mlc: Int? = 0,
    val is_rapid_test: Boolean? = false,
    val lab_to_facility_uuid: Int? = 0,
    val mobile: String? = "",
    val no_symptom: Boolean? = false,
    val period_uuid: Int? = 0,
    val pincode: String? = "",
    val professional_title_uuid: Int? = 0,
    val registred_facility_uuid: String? = "",
    val sample_type_uuid: Int? = 0,
    val sari: Boolean? = false,
    val saveExists: Boolean? = false,
    val session_uuid: Int? = 0,
    val state_uuid: Int? = 0,
    val suffix_uuid: Int? = 0,
    val taluk_uuid: Int? = 0,
    val title_uuid: Int? = 0,
    val village_uuid: Int? = 0,
    val weight: String? = ""
)