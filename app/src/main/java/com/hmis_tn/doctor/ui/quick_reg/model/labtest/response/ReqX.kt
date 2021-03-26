package com.hmis_tn.doctor.ui.quick_reg.model.labtest.response

data class ReqX(
    val auth_status_uuid: String? = "",
    val fromDate: String? = "",
    val from_facility_uuid: String? = "",
    val is_approved_required: Int? = 0,
    val is_requied_test_approval_list: Boolean? = false,
    val lab_uuid: Any? = Any(),
    val order_number: String? = "",
    val order_status_uuids: List<Int?>? = listOf(),
    val pageNo: Int? = 0,
    val paginationSize: Int? = 0,
    val pinOrMobile: String? = "",
    val qualifier_filter: String? = "",
    val search: String? = "",
    val sortField: String? = "",
    val sortOrder: String? = "",
    val test_name: String? = "",
    val toDate: String? = "",
    val to_facility_name: String? = "",
    val to_location_uuid: Any? = Any(),
    val widget_filter: String? = ""
)