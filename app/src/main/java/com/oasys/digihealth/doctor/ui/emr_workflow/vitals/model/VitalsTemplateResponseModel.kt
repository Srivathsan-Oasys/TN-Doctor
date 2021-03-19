package com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model

data class VitalsTemplateResponseModel(
    val req: String = "",
    val responseContents: VitalTemplateResponseContents = VitalTemplateResponseContents(),
    val statusCode: Int = 0
)