package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response

data class TreatmentPrescRouteSpinnerResponse(
    val responseContents: List<TreatmentPrescRouteSpinnerresponseContent?>? = listOf(),
    val message: String? = "",
    val req: Req? = Req(),
    val status: Int? = 0,
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)