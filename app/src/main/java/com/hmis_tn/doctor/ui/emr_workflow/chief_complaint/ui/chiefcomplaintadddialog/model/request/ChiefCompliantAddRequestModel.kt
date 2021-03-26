package com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.model.request

data class ChiefCompliantAddRequestModel(
    var details: List<Detail> = listOf(),
    var headers: Headers = Headers()
)