package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.nurse_desk.revertresponse

data class RevertResponseModel(
    var code: Int = 0,
    var message: String = "",
    var responseContent: RecertData = RecertData()
)