package com.hmis_tn.doctor.ui.emr_workflow.diet.model.template_department

data class GetAllDepartmentReq(
    var attributes: List<String> = listOf(),
    var facilityBased: Boolean = false,
    var pageNo: Int = 0,
    var paginationSize: Int = 0,
    var search: String = ""
)