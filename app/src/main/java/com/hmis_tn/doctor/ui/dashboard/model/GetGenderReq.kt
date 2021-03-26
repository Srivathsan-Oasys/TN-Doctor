package com.hmis_tn.doctor.ui.dashboard.model

data class GetGenderReq(
    var codename: String?,
    var is_active: Int?,
    var pageNo: Int?,
    var paginationSize: Int?,
    var search: String?,
    var sortField: String?,
    var sortOrder: String?
)