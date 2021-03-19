package com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.request

data class Detail(
    var Id: Int? = 0,
    var auth_status_uuid: Any? = Any(),
    var order_number: Int? = 0,
    var order_status_uuid: Int? = 0,
    var test_method_uuid: Int? = 0,
    var to_facility_uuid: Any? = Any(),
    var to_location_uuid: Int? = 0
)