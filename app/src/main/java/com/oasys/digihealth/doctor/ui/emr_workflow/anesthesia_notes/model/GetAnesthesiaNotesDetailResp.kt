package com.oasys.digihealth.doctor.ui.emr_workflow.anesthesia_notes.model

data class GetAnesthesiaNotesDetailResp(
    var code: Int?,
    var message: String?,
    var responseContents: List<ResponseContentX>?
)