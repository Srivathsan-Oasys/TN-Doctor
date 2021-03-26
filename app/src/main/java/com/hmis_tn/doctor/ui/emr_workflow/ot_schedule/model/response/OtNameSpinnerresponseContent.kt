package com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.response

data class OtNameSpinnerresponseContent(
    val institution_code: String? = "",
    val institution_name: String? = "",
    val institution_uuid: Int? = 0,
    val om_code: String? = "",
    val om_name: String? = "",
    val om_uuid: Int? = 0
)