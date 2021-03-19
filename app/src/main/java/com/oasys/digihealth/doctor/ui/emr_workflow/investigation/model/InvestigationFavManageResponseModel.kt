package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model

data class InvestigationFavManageResponseModel(
    val code: Int = 0,
    val message: String = "",
    val responseContents: InvestigationFavManageResponseContents = InvestigationFavManageResponseContents()
)