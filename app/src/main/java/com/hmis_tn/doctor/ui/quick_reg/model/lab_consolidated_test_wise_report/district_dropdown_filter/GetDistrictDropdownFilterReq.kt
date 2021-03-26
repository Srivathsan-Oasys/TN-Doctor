package com.hmis_tn.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.district_dropdown_filter

data class GetDistrictDropdownFilterReq(
    val institution_Id: List<Int>?,
    val user_Id: String?
)