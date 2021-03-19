package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery.spinners

data class SurgerySearchNameResponseModel(
    val responseContents: List<SurgerySearchNameresponseContent?>? = listOf(),
    val req: ReqX? = ReqX(),
    val status: String? = "",
    val statusCode: Int? = 0
)