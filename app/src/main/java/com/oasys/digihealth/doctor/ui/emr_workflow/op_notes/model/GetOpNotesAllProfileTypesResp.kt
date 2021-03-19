package com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model

data class GetOpNotesAllProfileTypesResp(
    var code: Int?,
    var message: String?,
    var responseContents: List<ResponseContent>?
)