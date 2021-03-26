package com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.model

data class NewLmisOrderModule(
    var message: String? = "",
    var responseContents: List<ResponseContentslmisorder?>? = listOf(),
    var statusCode: Int? = 0,
    var totalRecords: Int? = 0
)