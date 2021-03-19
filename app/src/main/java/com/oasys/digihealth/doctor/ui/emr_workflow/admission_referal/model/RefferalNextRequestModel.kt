package com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model

data class RefferalNextRequestModel(
    var department_uuid: String = "",
    var encounter_type_uuid: Int = 0,
    var encounter_uuid: Int = 0,
    var facility_uuid: String = "",
    var patient_uuid: String = "",
    var referal_reason_uuid: String = "",
    var referral_comments: String = "",
    var referral_deptartment_uuid: String = "",
    var referral_facility_uuid: String = "",
    var referral_type_uuid: Int = 0,
    var referred_date: String = ""
)