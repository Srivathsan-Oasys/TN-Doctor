package com.hmis_tn.doctor.ui.quick_reg.model.labtest.response

data class LabTestSpinnerResponseModel(
    val responseContents: List<LabTestSpinnerresponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)