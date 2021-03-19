package com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model

data class GetOpNotesEncounterByDocAndPatientIdResp(
    var code: Int?,
    var message: String?,
    var responseContents: List<ResponseContentXX>?
)