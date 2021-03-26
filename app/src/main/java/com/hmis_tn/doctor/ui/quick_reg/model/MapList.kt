package com.hmis_tn.doctor.ui.quick_reg.model

data class MapList(
    var created_by: Int = 0,
    var created_date: String = "",
    var facility_uuid: Int = 0,
    var from_department_uuid: Int = 0,
    var is_active: Boolean = false,
    var modified_by: Int = 0,
    var modified_date: String = "",
    var profile_uuid: Any = Any(),
    var revision: Any = Any(),
    var status: Boolean = false,
    var test_master_uuid: Int = 0,
    var to_department_uuid: Int = 0,
    var to_location_uuid: Int = 0,
    var to_subdepartment_uuid: Any = Any(),
    var uuid: Int = 0,
    var ward_uuid: Any = Any()
)