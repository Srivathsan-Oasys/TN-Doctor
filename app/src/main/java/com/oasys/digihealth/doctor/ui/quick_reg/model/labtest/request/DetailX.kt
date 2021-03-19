package com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.request

data class DetailX(
    var patient_order_test_detail_uuids: Int = 0,
    var qualifier_uuid: Int = 0,
    var result_value: String = ""
)