package com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.template.response

data class ResponseContentTemplateResponse(
    var details: List<Detail?>? = listOf(),
    var headers: Headers? = Headers()
)