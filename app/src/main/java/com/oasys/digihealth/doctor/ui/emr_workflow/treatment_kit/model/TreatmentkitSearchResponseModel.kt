package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model

data class TreatmentkitSearchResponseModel(
    val responseContents: List<TreatmentkitSearchresponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)