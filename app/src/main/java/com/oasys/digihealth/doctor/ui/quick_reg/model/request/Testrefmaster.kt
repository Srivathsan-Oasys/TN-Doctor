package com.oasys.digihealth.doctor.ui.quick_reg.model.request

data class Testrefmaster(
    var age_from: Int = 0,
    var age_to: Int = 0,
    var created_by: Int = 0,
    var created_date: String = "",
    var gender_uuid: Int = 0,
    var is_active: Boolean = false,
    var max_value: Int = 0,
    var min_value: Int = 0,
    var modified_by: Int = 0,
    var modified_date: String = "",
    var ref_value: Any = Any(),
    var revision: Any = Any(),
    var status: Boolean = false,
    var test_master_uuid: Int = 0,
    var uuid: Int = 0
)