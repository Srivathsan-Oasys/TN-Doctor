package com.hmis_tn.doctor.ui.institution.lmis.model

data class LocationMasterResponseModel(
    var responseContents: List<LocationMaster> = listOf(),
    var message: String = "",
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)