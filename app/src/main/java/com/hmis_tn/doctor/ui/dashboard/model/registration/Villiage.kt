package com.hmis_tn.doctor.ui.dashboard.model.registration

data class Villiage(
    var block_uuid: Any = Any(),
    var code: String = "",
    var color: Int = 0,
    var created_by: Int = 0,
    var created_date: String = "",
    var display_order: Int = 0,
    var hvillage_code: String = "",
    var is_active: Boolean = false,
    var language: Int = 0,
    var latitude: Any = Any(),
    var longitude: Any = Any(),
    var modified_by: Int = 0,
    var modified_date: String = "",
    var name: String = "",
    var regional_name: String = "",
    var short_code: String = "",
    var status: Boolean = false,
    var taluk_uuid: Int = 0,
    var uuid: Int = 0
)