package com.hmis_tn.doctor.ui.dashboard.model.registration

data class Taluk(
    var code: String = "",
    var created_by: Int = 0,
    var created_date: String = "",
    var district_master: DistrictMaster = DistrictMaster(),
    var district_uuid: Int = 0,
    var is_active: Boolean = false,
    var latitude: Any = Any(),
    var longitude: Any = Any(),
    var modified_by: Int = 0,
    var modified_date: String = "",
    var name: String = "",
    var regional_name: String = "",
    var short_code: String = "",
    var status: Boolean = false,
    var uuid: Int = 0
)