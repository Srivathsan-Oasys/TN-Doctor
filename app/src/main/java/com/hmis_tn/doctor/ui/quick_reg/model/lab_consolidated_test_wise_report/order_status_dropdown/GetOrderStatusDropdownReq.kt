package com.hmis_tn.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.order_status_dropdown

data class GetOrderStatusDropdownReq(
    val enddate: String?,
    val institution_Id: List<Int>?,
    val startdate: String?,
    val user_Id: String?
)