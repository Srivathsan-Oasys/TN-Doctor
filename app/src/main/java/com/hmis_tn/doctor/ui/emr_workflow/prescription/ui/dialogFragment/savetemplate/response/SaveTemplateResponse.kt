package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate.response

data class SaveTemplateResponse(
    var details: List<Detail> = listOf(),
    var headers: Headers = Headers()
)