package com.hmis_tn.doctor.ui.detailedRegistration.model

data class GetPatientAllReferral(
    var complication_details: Any? = null,
    var complication_uuid: Int? = null,
    var created_by: Int? = null,
    var created_date: String? = null,
    var department_uuid: Int? = null,
    var facility_type_uuid: Int? = null,
    var facility_uuid: Int? = null,
    var is_active: Boolean? = null,
    var modified_by: Int? = null,
    var modified_date: String? = null,
    var patient_uuid: Int? = null,
    var refer_other_institution: String? = null,
    var referral_reason_details: String? = null,
    var referral_type_details: String? = null,
    var refferal_date: String? = null,
    var refferal_reason_uuid: Int? = null,
    var refferal_type_uuid: Int? = null,
    var revision: Boolean? = null,
    var status: Boolean? = null,
    var uuid: Int? = null
)