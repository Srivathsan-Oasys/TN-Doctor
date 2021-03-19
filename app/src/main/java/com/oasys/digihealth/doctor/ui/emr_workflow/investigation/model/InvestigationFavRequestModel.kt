package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model

data class InvestigationFavRequestModel(
    var details: List<DetailX> = listOf(),
    var headers: HeadersX = HeadersX()
)