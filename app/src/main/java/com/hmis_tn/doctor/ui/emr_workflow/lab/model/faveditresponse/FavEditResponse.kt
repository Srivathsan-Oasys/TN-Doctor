package com.hmis_tn.doctor.ui.emr_workflow.lab.model.faveditresponse

data class FavEditResponse(
    var code: Int? = 0,
    var message: String? = "",
    var requestContent: RequestContentfaveditresponse? = RequestContentfaveditresponse()
)