package com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.request

data class AdmissionSaveRequestModel(
    var facility_uuid: String? = "",
    var department_uuid: String? = "",
    var encounter_type_uuid: Int? = 0,
    var referred_date: String? = "",
    var referral_type_uuid: Int? = 0,
    var referral_facility_uuid: String? = "",
    var referral_deptartment_uuid: Int? = 0,
    var referal_reason_uuid: String? = "",
    var referral_comments: String? = "",
    var encounter_uuid: Int? = 0,
    var patient_uuid: String? = "",
    var ward_uuid: Int? = 0
)