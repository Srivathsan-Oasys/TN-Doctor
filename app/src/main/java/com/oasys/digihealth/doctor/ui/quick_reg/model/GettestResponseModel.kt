package com.oasys.digihealth.doctor.ui.quick_reg.model

data class GettestResponseModel(
    var responseContents: List<GetTestMasterList> = listOf(),
    var message: String = "",
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)