package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.templeteEdit.response

data class UpdateResponseModel(
    var code: Int = 0,
    var message: String = "",
    var responseContent: ResponseContent = ResponseContent()
)