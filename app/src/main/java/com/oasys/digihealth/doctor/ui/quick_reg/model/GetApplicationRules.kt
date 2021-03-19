package com.oasys.digihealth.doctor.ui.quick_reg.model

data class GetApplicationRules(
    var created_by: Int = 0,
    var created_date: String = "",
    var field_name: String = "",
    var field_value: String = "",
    var is_active: Boolean = false,
    var modified_by: Int = 0,
    var modified_date: String = "",
    var revision: Any = Any(),
    var uuid: Int = 0
)