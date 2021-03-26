package com.hmis_tn.doctor.ui.quick_reg.model.labtest.response

data class RejectReferenceResponseModel(
    var responseContents: List<RejectReference> = listOf(),
    var req: String = "",
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)