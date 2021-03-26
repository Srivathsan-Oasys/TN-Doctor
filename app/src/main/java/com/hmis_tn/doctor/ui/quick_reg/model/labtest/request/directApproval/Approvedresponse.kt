package com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.directApproval

data class Approvedresponse(
    var count: Int = 0,
    var rows: List<ApprovedData> = listOf()
)