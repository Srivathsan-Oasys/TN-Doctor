package com.hmis_tn.doctor.ui.quick_reg.model.activitysession

data class ResponseActivitySession(
    var req: String? = "",
    var responseContents: List<ResponseContentsactivitysession?>? = listOf(),
    var statusCode: Int? = 0
)