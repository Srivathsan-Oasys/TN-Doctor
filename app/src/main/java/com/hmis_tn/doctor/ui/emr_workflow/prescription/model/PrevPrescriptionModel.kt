package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

data class PrevPrescriptionModel(
    var msg: String? = "",
    var req: Req? = Req(),
    var responseContents: PrevPrescriptionResponseContents? = PrevPrescriptionResponseContents(),
    var statusCode: Int? = 0
)