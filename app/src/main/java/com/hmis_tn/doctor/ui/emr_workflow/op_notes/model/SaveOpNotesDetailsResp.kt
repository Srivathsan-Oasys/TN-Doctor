package com.hmis_tn.doctor.ui.emr_workflow.op_notes.model

data class SaveOpNotesDetailsResp(
    var code: Int?,
    var message: String?,
    var reqContents: List<ReqContent>?
)