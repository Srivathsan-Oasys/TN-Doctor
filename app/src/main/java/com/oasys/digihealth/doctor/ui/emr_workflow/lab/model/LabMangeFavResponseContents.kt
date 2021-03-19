package com.oasys.digihealth.doctor.ui.emr_workflow.lab.model

data class LabMangeFavResponseContents(
    val details: List<FavResponseDetail> = listOf(),
    val headers: FavResponseHeaders = FavResponseHeaders()
)