package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model

data class InvestigationFavManageResponseContents(
    val details: List<Detail> = listOf(),
    val headers: Headers = Headers()
)