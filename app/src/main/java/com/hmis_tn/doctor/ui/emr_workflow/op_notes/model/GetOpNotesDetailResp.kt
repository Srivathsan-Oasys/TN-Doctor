package com.hmis_tn.doctor.ui.emr_workflow.op_notes.model

data class GetOpNotesDetailResp(
    var code: Int?,
    var message: String?,
    var responseContents: List<ResponseContentX>?
)