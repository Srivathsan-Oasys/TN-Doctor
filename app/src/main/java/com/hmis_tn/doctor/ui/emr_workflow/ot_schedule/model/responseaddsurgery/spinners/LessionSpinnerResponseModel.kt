package com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery.spinners

data class LessionSpinnerResponseModel(
    val responseContents: List<LessionSpinnerresponseContent?>? = listOf(),
    val req: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)