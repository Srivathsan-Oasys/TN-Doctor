package com.hmis_tn.doctor.ui.quickregistration.model

data class GetAllResponseModel(
    var responseContents: List<GetAllResponse> = listOf(),
    var req: String = "",
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)