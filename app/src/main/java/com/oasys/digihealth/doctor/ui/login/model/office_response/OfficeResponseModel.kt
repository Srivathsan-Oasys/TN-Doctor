package com.oasys.digihealth.doctor.ui.login.model.office_response

data class OfficeResponseModel(
    var responseContents: List<OfficeResponseContent?>? = listOf(),
    var statusCode: Int? = 0
)