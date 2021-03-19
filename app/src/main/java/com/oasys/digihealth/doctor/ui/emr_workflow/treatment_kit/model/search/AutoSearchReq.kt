package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.search

data class AutoSearchReq(
    val departmentId: String?,
    val pageNo: Int?,
    val paginationSize: Int?,
    val searchKey: String?,
    val searchValue: String?
)