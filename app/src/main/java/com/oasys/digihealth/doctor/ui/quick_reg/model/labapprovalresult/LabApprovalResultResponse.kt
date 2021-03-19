package com.oasys.digihealth.doctor.ui.quick_reg.model.labapprovalresult

data class LabApprovalResultResponse(
    val req: Req = Req(),
    val responseContents: LabApprovalResultResponseContents = LabApprovalResultResponseContents(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)