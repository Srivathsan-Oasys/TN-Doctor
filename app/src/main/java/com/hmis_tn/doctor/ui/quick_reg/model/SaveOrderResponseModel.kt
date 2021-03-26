package com.hmis_tn.doctor.ui.quick_reg.model

data class SaveOrderResponseModel(
    var responseContents: List<SaveOrder> = listOf(),
    var message: String = "",
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)