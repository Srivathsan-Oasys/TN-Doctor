package com.oasys.digihealth.doctor.ui.quick_reg.model

data class LocationMasterX(
    var created_by: Int = 0,
    var created_by_name: String = "",
    var created_date: String = "",
    var department_name: String = "",
    var department_uuid: Int = 0,
    var description: String = "",
    var facility_name: String = "",
    var facility_uuid: Int = 0,
    var is_active: Boolean = false,
    var lab_master_type_uuid: Int = 0,
    var loc_block_uuid: Int = 0,
    var loc_building_uuid: Int = 0,
    var loc_floor_uuid: Int = 0,
    var loc_room_uuid: Int = 0,
    var location_code: String = "",
    var location_name: String = "",
    var modified_by: Int = 0,
    var modified_by_name: String = "",
    var modified_date: String = "",
    var revision: Int = 0,
    var status: Boolean = false,
    var sub_department_name: String = "",
    var sub_department_uuid: Int = 0,
    var uuid: Int = 0
)