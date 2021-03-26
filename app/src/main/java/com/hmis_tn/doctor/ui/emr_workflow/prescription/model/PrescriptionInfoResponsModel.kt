package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

data class PrescriptionInfoResponsModel(
    val message: String = "",
    val req: Req = Req(),
    val responseContents: PrescriptionInfoResponseContents = PrescriptionInfoResponseContents(),
    val status: Int = 0,
    val statusCode: Int = 0
)