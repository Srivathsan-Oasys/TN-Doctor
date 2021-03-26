package com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery.spinners

data class ReqX(
    val department_uuid: String? = "",
    val facility_uuid: String? = "",
    val pageNo: Int? = 0,
    val paginationSize: Int? = 0,
    val searchkey: String? = "",
    val sortField: String? = "",
    val sortOrder: String? = ""
)