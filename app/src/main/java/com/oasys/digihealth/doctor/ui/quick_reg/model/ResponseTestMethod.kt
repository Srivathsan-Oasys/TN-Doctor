package com.oasys.digihealth.doctor.ui.quick_reg.model

data class ResponseTestMethod(
    var req: String? = "",
    var responseContents: List<ResponseTestMethodContent?>? = listOf(),
    var statusCode: Int? = 0,
    var totalRecords: Int? = 0
)