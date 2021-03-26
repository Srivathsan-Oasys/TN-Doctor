package com.hmis_tn.doctor.ui.emr_workflow.anesthesia_notes.model

data class GetAnesthesiaNotesEncounterByDocAndPatientIdResp(
    var code: Int?,
    var message: String?,
    var responseContents: List<ResponseContentXX>?
)