package com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response

data class UserProfileResponseModel(
    var responseContents: List<UserProfileResponse> = listOf(),
    var msg: String = "",
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)