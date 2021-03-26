package com.hmis_tn.doctor.ui.emr_workflow.ip_case_sheet.model

data class SaveCaseSheetDetailsResp(
    var code: Int?,
    var message: String?,
    var reqContents: List<ReqContent>?
)