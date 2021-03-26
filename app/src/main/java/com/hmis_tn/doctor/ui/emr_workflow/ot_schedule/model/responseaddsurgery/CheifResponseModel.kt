package com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery

data class CheifResponseModel(
    val responseContents: List<CheifresponseContent?>? = listOf(),
    val req: Req? = Req(),
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)