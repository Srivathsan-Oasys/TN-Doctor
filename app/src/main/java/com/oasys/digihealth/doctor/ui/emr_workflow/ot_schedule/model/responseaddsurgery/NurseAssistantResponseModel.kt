package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery

data class NurseAssistantResponseModel(
    val responseContents: List<NurseAssistantresponseContent?>? = listOf(),
    val req: ReqXX? = ReqXX(),
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)