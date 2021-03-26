package com.hmis_tn.doctor.ui.emr_workflow.investigation.model

data class InvestigationFavRequestModel(
    var details: List<DetailX> = listOf(),
    var headers: HeadersX = HeadersX()
)