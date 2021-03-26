package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response

data class TreatmentNameResponseModel(
    val responseContents: List<TreatmentNameresponseContent?>? = listOf(),
    val code: Int? = 0,
    val message: String? = "",
    val responseLength: Int? = 0
)