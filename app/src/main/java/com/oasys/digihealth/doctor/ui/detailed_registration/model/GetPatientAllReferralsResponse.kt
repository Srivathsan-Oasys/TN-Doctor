package com.oasys.digihealth.doctor.ui.detailedRegistration.model

data class GetPatientAllReferralsResponse(
    var responseContents: List<GetPatientAllReferral>? = null,
    var statusCode: Int? = null
)