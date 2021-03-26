package com.hmis_tn.doctor.ui.emr_workflow.vitals.model

data class VitalsSearchResponseModel(
    val message: String = "",
    val responseContents: VitalsSearchResponseContents = VitalsSearchResponseContents(),
    val statusCode: Int = 0
)