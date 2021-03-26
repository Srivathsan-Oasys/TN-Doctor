package com.hmis_tn.doctor.ui.quickregistration.model.response

data class QuickelementRoleResponseModel(
    var responseContents: List<QuickelementRoleResponse> = listOf(),
    var statusCode: Int = 0
)