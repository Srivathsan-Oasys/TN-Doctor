package com.hmis_tn.doctor.ui.helpdesk.model

data class VendorResponseContent(
    val vendor_code: String? = "",
    val vendor_name: String? = "",
    val uuid: Int? = 0,
    val uhid: String? = "",
    val age: Int? = 0,
    val status: Boolean? = false,
    val vendor_type_uuid: Int? = 0,
    val is_active: Boolean? = false,
    val is_asset_vendor: Boolean? = false,
    val revision: Int? = 0,
    val vendor_url: String? = "",
    val licence_code: String? = "",
    val first_name: String? = "",
    val dob: String? = "",
    val lead_time: Int? = 0,
    val business_domain_uuid: Int? = 0,
    val distribution_type_uuid: Int? = 0,
    val payment_terms_uuid: Int? = 0,
    val payment_type_uuid: Int? = 0,
    val currency_code_uuid: Int? = 0,
    val item_category_uuid: Int? = 0,
    val licence_number: String? = "",
    val contact_person: String? = "",
    val landline_number: String? = "",
    val mobile_number: String? = "",
    val mobile1: String? = "",
    val fax_number: String? = "",
    val email_id: String? = "",
    val email: String? = "",
    val created_date: String? = "",
    val modified_date: String? = "",
    val vendor_type: VendorType? = null,
    val patient_detail: PatientDetail? = null,
    val patient_visits: List<PatientVisits?>? = listOf(),
    val gender_details: VendorType? = null,
    val gender: VendorType? = null,
    val facility_details: VendorType? = null,
    val department: VendorType? = null,
    val facility: VendorType? = null
) {
    data class VendorType(
        val code: String? = null,
        val name: String? = null,
        val uuid: Int? = null
    )

    data class PatientDetail(
        val mobile: String? = null,
        val smart_ration_number: String? = null,
        val patient_uuid: Int? = null
    )

    data class PatientVisits(
        val registered_date: String? = null,
        val facility_uuid: Int? = null,
        val patient_uuid: Int? = null,
        val patient_type_uuid: Int? = null,
        val department_uuid: Int? = null,
        val patient_type_detail: VendorType? = null,
        val department_details: VendorType? = null,
        val facility_details: VendorType? = null
    )

}