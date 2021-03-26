package com.hmis_tn.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.block_dropdown

data class GetBlockDropdownResp(
    val message: String?,
    val req: String?,
    val responseContents: List<ResponseContent>?,
    val statusCode: Int?
)