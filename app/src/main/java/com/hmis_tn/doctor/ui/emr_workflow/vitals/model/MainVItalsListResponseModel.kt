package com.hmis_tn.doctor.ui.emr_workflow.vitals.model

data class MainVItalsListResponseModel(
    val responseContents: MainVitalsListresponseContents? = MainVitalsListresponseContents(),
    val message: String? = "",
    val statusCode: Int? = 0
)