package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.response

data class AdmissionUpdateRespModel(
    val req: Admisionreq? = Admisionreq(),
    val msg: String? = "",
    val responseContents: List<Any>? = listOf(),
    val statusCode: Int? = 0
)