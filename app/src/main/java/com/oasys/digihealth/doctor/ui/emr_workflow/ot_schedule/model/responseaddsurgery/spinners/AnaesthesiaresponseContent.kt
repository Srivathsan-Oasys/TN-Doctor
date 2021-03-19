package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery.spinners

data class AnaesthesiaresponseContent(
    val Is_default: Boolean? = false,
    val code: String? = "",
    val created_by: Int? = 0,
    val created_date: String? = "",
    val display_order: Int? = 0,
    val is_active: Boolean? = false,
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    val name: String? = "",
    val revision: Revision? = Revision(),
    val status: Boolean? = false,
    val uuid: Int? = 0
)