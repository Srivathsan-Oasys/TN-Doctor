package com.hmis_tn.doctor.ui.emr_workflow.ot_notes.model

data class GetOtNotesDetailResp(
    var code: Int?,
    var message: String?,
    var responseContents: List<ResponseContentX>?
)