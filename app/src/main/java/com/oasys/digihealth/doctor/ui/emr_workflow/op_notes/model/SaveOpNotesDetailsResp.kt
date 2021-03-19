package com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model

data class SaveOpNotesDetailsResp(
    var code: Int?,
    var message: String?,
    var reqContents: List<ReqContent>?
)