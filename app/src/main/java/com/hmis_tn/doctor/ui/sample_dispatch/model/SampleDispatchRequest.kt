package com.hmis_tn.doctor.ui.sample_dispatch.model

data class SampleDispatchRequest(
    var fromDate: String = "",
    var lab_uuid: String? = null,
    var order_number: String = "",
    var order_priority_uuid: String = "",
    var order_status_uuid: List<Int> = listOf(),
    var pageNo: Int = 0,
    var paginationSize: Int = 0,
    var pinOrMobile: String = "",
    var qualifier_filter: String = "",
    var search: String = "",
    var test_method_name: String = "",
    var test_name: String = "",
    var toDate: String = "",
    var to_facility_name: String = "",
    var to_location_uuid: String? = null,
    var widget_filter: String = "",
    var is_sample_dispatch: Boolean = true

)