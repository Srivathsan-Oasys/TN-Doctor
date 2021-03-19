package com.oasys.digihealth.doctor.ui.quick_reg.model

data class GetApplicationRulesResponseModel(
    var responseContents: ArrayList<GetApplicationRules> = ArrayList(),
    var currentDateTime: String = "",
    var req: String = "",
    var statusCode: Int = 0
)