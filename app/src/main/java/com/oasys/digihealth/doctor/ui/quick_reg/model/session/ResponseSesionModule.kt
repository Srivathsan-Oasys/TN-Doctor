package com.oasys.digihealth.doctor.ui.quick_reg.model.session

data class ResponseSesionModule(
    var req: String? = "",
    var responseContents: ResponseContentsSession? = ResponseContentsSession(),
    var statusCode: Int? = 0
)