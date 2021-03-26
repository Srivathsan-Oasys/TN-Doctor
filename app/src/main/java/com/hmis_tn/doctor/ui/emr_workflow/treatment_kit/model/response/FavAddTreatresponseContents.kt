package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response

data class FavAddTreatresponseContents(
    val details: List<Detail?>? = listOf(),
    val headers: Headers? = Headers()
)