package com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.model.response

data class GetWardIdResponseModel(
    var message: String? = null,
    var statusCode: Int? = null,
    var ward_uuid: Int? = null
)