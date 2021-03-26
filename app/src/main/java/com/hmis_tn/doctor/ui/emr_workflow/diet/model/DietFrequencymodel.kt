package com.hmis_tn.doctor.ui.emr_workflow.diet.model

data class DietFrequencyResponse(
    val code: Int? = null,
    val responseContents: List<DietFrequencyData?>? = null
)

data class DietFrequencyData(
    val code: String? = null,
    val name: String? = null,
    val uuid: Int? = null
)
