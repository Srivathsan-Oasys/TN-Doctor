package com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.model.response

data class ResponseLmisListview(
    var message: String? = "",
    var responseContents: List<ResponseContentslmislistview?>? = listOf(),
    var statusCode: Int? = 0,
    var totalRecords: Int? = 0
)