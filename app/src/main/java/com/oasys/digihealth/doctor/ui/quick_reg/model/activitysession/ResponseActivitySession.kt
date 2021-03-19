package com.oasys.digihealth.doctor.ui.quick_reg.model.activitysession

data class ResponseActivitySession(
    var req: String? = "",
    var responseContents: List<ResponseContentsactivitysession?>? = listOf(),
    var statusCode: Int? = 0
)