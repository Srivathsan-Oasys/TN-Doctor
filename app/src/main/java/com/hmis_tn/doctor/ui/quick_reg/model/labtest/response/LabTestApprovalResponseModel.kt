package com.hmis_tn.doctor.ui.quick_reg.model.labtest.response

data class LabTestApprovalResponseModel(
    val responseContents: List<LabTestApprovalresponseContent>? = listOf(),
    val disease_result_data: List<DiseaseResultDataX?>? = listOf(),
    val message: String? = "",
    val order_status_count: List<OrderStatusCountX?>? = listOf(),
    val req: ReqX? = ReqX(),
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)