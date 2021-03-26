package com.hmis_tn.doctor.ui.emr_workflow.lab.model

data class LabFavManageResponseModel(
    val code: Int = 0,
    val message: String = "",
    val responseContents: LabMangeFavResponseContents = LabMangeFavResponseContents()
)