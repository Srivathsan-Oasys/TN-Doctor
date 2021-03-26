package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.response

data class AdmissionPatientRefresponseContent(
    val comments: Any? = Any(),
    val created_by: Int? = 0,
    val created_date: String? = "",
    val department_uuid: Int? = 0,
    val encounter_type_uuid: Int? = 0,
    val encounter_uuid: Int? = 0,
    val facility_uuid: Int? = 0,
    val is_active: Boolean? = false,
    val is_admitted: Boolean? = false,
    val is_reviewed: Boolean? = false,
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    val patient_uuid: Int? = 0,
    val referal_reason_uuid: Int? = 0,
    val referral_comments: String? = "",
    val referral_deptartment_uuid: Int? = 0,
    val referral_facility_uuid: Int? = 0,
    val referral_type_uuid: Int? = 0,
    val referred_by: Int? = 0,
    val referred_date: String? = "",
    val revision: Int? = 0,
    val status: Boolean? = false,
    val uuid: Int? = 0,
    val ward_uuid: Int? = 0
)