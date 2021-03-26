package com.hmis_tn.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.lab_name_dropdown

data class GetLabNameDropdownReq(
    val institution_Id: List<Int>?,
    val user_Id: String?
)