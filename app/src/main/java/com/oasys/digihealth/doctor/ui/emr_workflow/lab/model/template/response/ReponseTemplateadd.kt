package com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.response

data class ReponseTemplateadd(
    var code: Int? = 0,
    var message: String? = "",
    var responseContent: ResponseContentTemplateResponse? = ResponseContentTemplateResponse()
)