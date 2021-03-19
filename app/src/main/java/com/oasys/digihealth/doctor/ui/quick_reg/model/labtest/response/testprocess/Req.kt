package com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response.testprocess

data class Req(
    val fromDate: String? = "",
    val is_requied_test_process_list: Boolean? = false,
    val lab_uuid: Any? = Any(),
    val order_number: String? = "",
    val order_status_uuids: List<Int?>? = listOf(),
    val pageNo: Int? = 0,
    val paginationSize: Int? = 0,
    val pinOrMobile: String? = "",
    val qualifier_filter: String? = "",
    val search: String? = "",
    val test_name: String? = "",
    val toDate: String? = "",
    val to_facility_name: String? = "",
    val to_location_uuid: Any? = Any(),
    val widget_filter: String? = ""
)