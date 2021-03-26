package com.hmis_tn.doctor.ui.emr_workflow.lab.model.updateresponse

data class UpdateResponse(
    var code: Int? = 0,
    var message: String? = "",
    var responseContent: UpdateEditResponseContent? = UpdateEditResponseContent()
)