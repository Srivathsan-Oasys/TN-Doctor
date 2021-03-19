package com.oasys.digihealth.doctor.ui.detailedRegistration.model

data class GetPatientAllVisitsResponse(
    var responseContents: List<GetPatientAllVisit>? = null,
    var message: String? = null,
    var statusCode: Int? = null,
    var totalRecords: Int? = null
)