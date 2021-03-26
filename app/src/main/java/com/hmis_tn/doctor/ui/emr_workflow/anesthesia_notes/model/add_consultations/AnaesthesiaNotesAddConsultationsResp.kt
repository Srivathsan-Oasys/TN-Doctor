package com.hmis_tn.doctor.ui.emr_workflow.anesthesia_notes.model.add_consultations

data class AnaesthesiaNotesAddConsultationsResp(
    var code: Int? = 0,
    var message: String? = "",
    var reqContents: ReqContents? = ReqContents(),
    var responseContents: ResponseContents? = ResponseContents()
)