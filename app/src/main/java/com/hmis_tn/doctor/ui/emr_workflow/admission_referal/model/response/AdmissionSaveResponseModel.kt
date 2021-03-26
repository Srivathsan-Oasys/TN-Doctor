package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.response

data class AdmissionSaveResponseModel(
    val responseContents: AdmissionSaveresponseContents? = AdmissionSaveresponseContents(),
    val message: String? = "",
    val req: Req? = Req(),
    val statusCode: Int? = 0
)