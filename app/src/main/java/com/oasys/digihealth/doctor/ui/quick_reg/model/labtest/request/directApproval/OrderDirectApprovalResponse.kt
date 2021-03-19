package com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.request.directApproval

data class OrderDirectApprovalResponse(
    var responseContents: Approvedresponse = Approvedresponse(),
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)