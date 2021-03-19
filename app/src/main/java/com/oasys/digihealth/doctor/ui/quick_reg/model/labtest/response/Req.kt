package com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response

data class Req(
    val fromDate: String? = "",
    val is_lab_test: Boolean? = false,
    val order_number: String? = "",
    val pageNo: Int? = 0,
    val paginationSize: Int? = 0,
    val pinOrMobile: String? = "",
    val search: String? = "",
    val test_name: String? = "",
    val toDate: String? = "",
    val to_facility_name: String? = "",
    val to_facility_uuid: String? = "",
    val to_location_uuid: Any? = Any()
)