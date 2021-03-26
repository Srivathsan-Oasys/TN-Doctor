package com.hmis_tn.doctor.ui.emr_workflow.vitals.model

data class VitalsListResponseModel(
    val message: String = "",
    val responseContents: VitalsListResponseContents = VitalsListResponseContents(),
    val statusCode: Int = 0
)