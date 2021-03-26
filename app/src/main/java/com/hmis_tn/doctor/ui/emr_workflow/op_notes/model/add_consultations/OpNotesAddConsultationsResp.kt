package com.hmis_tn.doctor.ui.emr_workflow.op_notes.model.add_consultations

import com.hmis_tn.doctor.ui.emr_workflow.ot_notes.model.add_consultations.ResponseContents

data class OpNotesAddConsultationsResp(
    var code: Int? = 0,
    var message: String? = "",
    var reqContents: ReqContents? = ReqContents(),
    var responseContents: ResponseContents? = ResponseContents()
)