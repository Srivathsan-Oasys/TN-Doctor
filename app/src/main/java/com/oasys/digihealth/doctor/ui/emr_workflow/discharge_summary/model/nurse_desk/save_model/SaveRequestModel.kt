package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.nurse_desk.save_model

data class SaveRequestModel(
    var details: List<Detail> = listOf(),
    var headers: Headers = Headers()
)