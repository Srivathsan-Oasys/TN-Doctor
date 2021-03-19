package com.oasys.digihealth.doctor.ui.quick_reg.model

data class ResponsePrivillageModule(
    var responseContents: List<ResponseContentsPrivillage?>? = listOf(),
    var req: Req? = Req(),
    var statusCode: Int? = 0
)