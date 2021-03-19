package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery.spinners

data class PositionResponseModel(
    val responseContents: List<PositionresponseContent?>? = listOf(),
    val req: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)