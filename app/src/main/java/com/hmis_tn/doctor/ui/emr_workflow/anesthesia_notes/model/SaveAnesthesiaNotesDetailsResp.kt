package com.hmis_tn.doctor.ui.emr_workflow.anesthesia_notes.model

data class SaveAnesthesiaNotesDetailsResp(
    var code: Int?,
    var message: String?,
    var reqContents: List<ReqContent>?
)