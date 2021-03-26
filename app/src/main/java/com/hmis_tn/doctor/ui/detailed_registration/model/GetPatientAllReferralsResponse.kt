package com.hmis_tn.doctor.ui.detailedRegistration.model

data class GetPatientAllReferralsResponse(
    var responseContents: List<GetPatientAllReferral>? = null,
    var statusCode: Int? = null
)