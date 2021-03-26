package com.hmis_tn.doctor.ui.emr_workflow.ip_case_sheet.model

data class GetCaseSheetDetailResp(
    var code: Int?,
    var message: String?,
    var responseContents: List<ResponseContentX>?
)