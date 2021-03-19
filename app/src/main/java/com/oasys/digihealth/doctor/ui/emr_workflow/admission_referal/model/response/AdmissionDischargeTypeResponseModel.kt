package com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.response

data class AdmissionDischargeTypeResponseModel(
    val req: String = "",
    val responseContents: List<AdmissionDischargeTypeResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)