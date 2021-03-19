package com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.order_status_dropdown

data class GetOrderStatusDropdownResp(
    val message: String?,
    val req: String?,
    val responseContents: List<ResponseContent>?,
    val statusCode: Int?
)