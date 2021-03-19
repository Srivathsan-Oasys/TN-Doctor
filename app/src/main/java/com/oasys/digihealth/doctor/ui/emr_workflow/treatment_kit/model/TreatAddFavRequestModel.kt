package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model

data class TreatAddFavRequestModel(
    var details: List<Detail?>? = listOf(),
    var headers: Headers? = Headers()
)