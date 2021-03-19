package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery.spinners

data class AnesthetistSpinnerResponseModel(
    val responseContents: List<AnesthetistSpinnerresponseContent?>? = listOf(),
    val req: Req? = Req(),
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)