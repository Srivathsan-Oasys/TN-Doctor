package com.oasys.digihealth.doctor.ui.quick_reg.model

data class BlockZone(
    var code: String = "",
    var created_by: Int = 0,
    var created_date: String = "",
    var district_uuid: Int = 0,
    var hud_uuid: Int = 0,
    var is_active: Boolean = false,
    var latitude: String = "",
    var longitude: String = "",
    var modified_by: Int = 0,
    var modified_date: String = "",
    var name: String = "",
    var regional_name: String = "",
    var revision: Int = 0,
    var short_code: String = "",
    var status: Boolean = false,
    var uuid: Int = 0
)