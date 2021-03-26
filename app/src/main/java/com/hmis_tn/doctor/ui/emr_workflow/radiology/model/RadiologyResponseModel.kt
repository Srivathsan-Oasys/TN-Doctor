package com.hmis_tn.doctor.ui.emr_workflow.radiology.model

data class RadiologyResponseModel(
    val message: String = "",
    val responseContents: RadiologyResponseContents = RadiologyResponseContents(),
    val statusCode: Int = 0
)