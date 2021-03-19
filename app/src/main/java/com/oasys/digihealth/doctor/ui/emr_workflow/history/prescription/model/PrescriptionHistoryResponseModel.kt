package com.oasys.digihealth.doctor.ui.emr_workflow.history.prescription.model

data class PrescriptionHistoryResponseModel(
    val msg: String = "",
    val req: Req = Req(),
    val responseContents: PrescriptionHistoryResponseContents = PrescriptionHistoryResponseContents(),
    val statusCode: Int = 0
)