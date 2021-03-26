package com.hmis_tn.doctor.ui.quick_reg.model

data class LocationMasterResponseModelX(
    var responseContents: List<LocationMasterX> = listOf(),
    var message: String = "",
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)