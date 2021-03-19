package com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.request

data class AdmissionPatientUpdateRequestModel(
    var patient_referral_uuid: Int? = 0,
    var referal_reason_uuid: Int? = 0,
    var referral_comments: String? = "",
    var referral_deptartment_uuid: Int? = 0,
    var referred_date: String? = "",
    var ward_uuid: Int? = 0
)