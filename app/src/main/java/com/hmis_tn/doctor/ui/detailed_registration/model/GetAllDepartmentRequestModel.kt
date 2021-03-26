package com.hmis_tn.doctor.ui.detailedRegistration.model

data class GetAllDepartmentRequestModel(
    var attributes: List<String>? = null,
    var casualtyBased: Boolean? = null,
    var facilityBased: Boolean? = null,
    var facilityId: Int? = null,
    var isClinical: Boolean? = null,
    var pageNo: Int? = null,
    var paginationSize: Int? = null,
    var registrationBased: Boolean? = null,
    var search: String? = null
)