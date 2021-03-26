package com.hmis_tn.doctor.ui.emr_workflow.ot_notes.model

data class SaveOtNotesDetailsResp(
    var code: Int?,
    var message: String?,
    var reqContents: List<ReqContent>?
)