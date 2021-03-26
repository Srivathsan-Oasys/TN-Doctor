package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.save_model

data class SaveRequestModel(
    var details: List<Detail> = listOf(),
    var headers: Headers = Headers()
)