package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.response

data class OtNameSpinnerResponseModel(
    val responseContents: List<OtNameSpinnerresponseContent?>? = listOf(),
    val msg: String? = "",
    val req: Req? = Req(),
    val status: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)