package com.oasys.digihealth.doctor.ui.emr_workflow.history.surgery.model.response

data class SurgeryUpdateResponseModel(
    val requestContent: List<Int?>? = listOf(),
    val code: Int? = 0,
    val message: String? = ""
)