package com.hmis_tn.doctor.ui.quick_reg.model.labtest.request

data class LabTestRequestModel(
    var fromDate: String? = "",
    var is_lab_test: Boolean? = false,
    var order_number: String? = "",
    var pageNo: Int? = 0,
    var paginationSize: Int? = 0,
    var pinOrMobile: String? = "",
    var search: String? = "",
    var test_name: String? = "",
    var toDate: String? = "",
    var test_method_name : String?="",
    var to_facility_name: String? = "",
    var to_facility_uuid: String? = "",
    var to_location_uuid: String? = null,
    var lab_uuid: String? = null
)