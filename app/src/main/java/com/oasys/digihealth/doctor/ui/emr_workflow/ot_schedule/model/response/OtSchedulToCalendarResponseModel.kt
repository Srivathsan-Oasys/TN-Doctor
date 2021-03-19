package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.response

data class OtSchedulToCalendarResponseModel(
    val responseContents: List<OtScheduleToCalendarresponseContent?>? = listOf(),
    val msg: String? = "",
    val rowCount: Int? = 0,
    val status: String? = "",
    val statusCode: Int? = 0
)