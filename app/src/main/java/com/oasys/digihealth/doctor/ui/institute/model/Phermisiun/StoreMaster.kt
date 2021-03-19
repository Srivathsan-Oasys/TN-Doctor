package com.oasys.digihealth.doctor.ui.institute.model.Phermisiun

data class StoreMaster(
    var active_from: Any? = null,
    var active_to: Any? = null,
    var can_allow_auto_indent: Boolean = false,
    var created_by: Int = 0,
    var created_date: String = "",
    var expirypriorstopdays: Int = 0,
    var expirywarningdays: Int = 0,
    var facility_uuid: Int = 0,
    var gender_uuid: Int = 0,
    var is_active: Boolean = false,
    var is_dot_matrix: Boolean = false,
    var is_laser: Boolean = false,
    var is_primary_store: Boolean = false,
    var is_virtual_store: Boolean = false,
    var license_no: Any? = null,
    var loc_block_uuid: Int = 0,
    var loc_building_uuid: Int = 0,
    var loc_floor_uuid: Int = 0,
    var loc_room_uuid: Int = 0,
    var modified_by: Int = 0,
    var modified_date: String = "",
    var non_moving_days: Int = 0,
    var revision: Int = 0,
    var status: Boolean = false,
    var store_category: StoreCategory = StoreCategory(),
    var store_category_uuid: Int = 0,
    var store_code: String = "",
    var store_description: String? = null,
    var store_name: String = "",
    var store_policy_uuid: Int = 0,
    var store_sub_category: StoreSubCategory = StoreSubCategory(),
    var store_sub_category_uuid: Int = 0,
    var store_type: StoreType = StoreType(),
    var store_type_uuid: Int = 0,
    var tin_no: Any? = null,
    var uuid: Int = 0
)