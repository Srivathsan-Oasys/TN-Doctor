package com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.response

data class RadiologyHistoryResponsemodel(
    val dataaaresponseContents: List<DataaaresponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0
)