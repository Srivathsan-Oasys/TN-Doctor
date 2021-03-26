package com.hmis_tn.doctor.ui.emr_workflow.lab_result.model.compare

data class LabCompareResp(
    var message: String? = "",
    var responseContents: List<ResponseContent>? = listOf(),
    var statusCode: Int? = 0,
    var totalRecords: Int? = 0
)