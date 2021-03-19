package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery

data class SurgeonResponseModel(
    val responseContents: List<SurgeonresponseContent?>? = listOf(),
    val req: ReqX? = ReqX(),
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)