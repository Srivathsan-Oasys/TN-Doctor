package com.oasys.digihealth.doctor.ui.detailedRegistration.model

data class GetPatientAllVisitsRequest(
    var pageNo: Int? = null,
    var paginationSize: Int? = null,
    var patientId: Int? = null,
    var sortField: String? = "registered_date",
    var sortOrder: String? = "DESC"
)

