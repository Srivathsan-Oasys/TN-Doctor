package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model

data class RequestRadiologyFavModel(
    var details: List<Detail> = listOf(),
    var headers: Headers = Headers()
)