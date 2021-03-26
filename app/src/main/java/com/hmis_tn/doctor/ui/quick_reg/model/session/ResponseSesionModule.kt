package com.hmis_tn.doctor.ui.quick_reg.model.session

data class ResponseSesionModule(
    var req: String? = "",
    var responseContents: ResponseContentsSession? = ResponseContentsSession(),
    var statusCode: Int? = 0
)