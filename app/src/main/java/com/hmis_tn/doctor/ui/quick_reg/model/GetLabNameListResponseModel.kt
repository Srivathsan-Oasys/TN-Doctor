package com.hmis_tn.doctor.ui.quick_reg.model

data class GetLabNameListResponseModel(
    var responseContent: GetLabNameList = GetLabNameList(),
    var statusCode: Int = 0
)