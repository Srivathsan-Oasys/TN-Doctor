package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model

data class ManageFavAddResponse(
    val code: Int = 0,
    val message: String = "",
    val responseContentLength: Int = 0,
    val responseContents: ManageFavAddResponseContents = ManageFavAddResponseContents()


)