package com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.favresponse

data class FavAddListResponse(
    var code: Int? = 0,
    var message: String? = "",
    var responseContentLength: Int? = 0,
    var responseContents: ResponseContentsfav? = ResponseContentsfav()
)