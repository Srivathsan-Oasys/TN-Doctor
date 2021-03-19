package com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response

data class LabTestResponseModel(
    val responseContents: List<LabTestresponseContent?>? = listOf(),
    val disease_result_data: List<DiseaseResultData?>? = listOf(),
    val message: String? = "",
    val order_status_count: List<OrderStatusCount?>? = listOf(),
    val req: Req? = Req(),
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)