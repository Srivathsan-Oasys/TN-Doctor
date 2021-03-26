package com.hmis_tn.doctor.ui.quick_reg.model.labapprovalresult.response

data class OrderApprovedResponseModel(
    var msg: String = "",
    var responseContents: List<ResponseContent> = listOf(),
    var statusCode: Int = 0
)