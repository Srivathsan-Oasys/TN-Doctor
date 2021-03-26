package com.hmis_tn.doctor.ui.quick_reg.model.labtest.request

data class TestProcessRequestModel(
    var fromDate: String? = "",
    var is_requied_test_process_list: Boolean? = false,
    var lab_uuid: String? = null,
    var order_number: String? = "",
    var order_status_uuids: List<Int?>? = listOf(),
    var pageNo: Int? = 0,
    var paginationSize: Int? = 0,
    var pinOrMobile: String? = "",
    var qualifier_filter: String? = "",
    var search: String? = "",
    var test_name: String? = "",
    var toDate: String? = "",
    var to_facility_name: String? = "",
    var to_location_uuid: String? = null,
    var widget_filter: String? = ""
)