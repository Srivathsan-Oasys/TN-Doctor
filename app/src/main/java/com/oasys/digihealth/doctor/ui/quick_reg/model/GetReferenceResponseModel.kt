package com.oasys.digihealth.doctor.ui.quick_reg.model

data class GetReferenceResponseModel(
    var responseContents: List<GetReference> = listOf(),
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)