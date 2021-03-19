package com.oasys.digihealth.doctor.ui.quick_reg.model

data class SearchPatientRequestModelCovid(
    var mobile: String? = null,
    var oldPin: String? = null,
    var pageNo: Int? = null,
    var paginationSize: Int? = null,
    var pin: String? = null,
    var aadhaar: String? = null,
    var sortField: String? = "",
    var sortOrder: String? = null,
    var searchKeyWord : String?=""
)