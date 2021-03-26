package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.favouriteEdit.updateresponse

data class ResponsePreFavEdit(
    var code: Int? = 0,
    var message: String? = "",
    var requestContent: RequestPreFavEditContent? = RequestPreFavEditContent()
)