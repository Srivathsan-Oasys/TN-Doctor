package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery.spinners

data class AnaeesthesiaSpinnerResponseModel(
    val responseContents: List<AnaesthesiaresponseContent?>? = listOf(),
    val req: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)