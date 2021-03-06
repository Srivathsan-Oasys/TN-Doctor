package com.hmis_tn.doctor.ui.quick_reg.model

data class GetLabNameList(
    var approval_user_uuid: String = "",
    var code: String = "",
    var created_by: Int = 0,
    var created_date: String = "",
    var description: String = "",
    var email: String = "",
    var facility_level_uuid: Int = 0,
    var facility_type_uuid: Int = 0,
    var fax: String = "",
    var health_office_uuid: Int = 0,
    var hud_uuid: Int = 0,
    var image_url: String = "",
    var is_active: Boolean = false,
    var is_facility_model: Boolean = false,
    var is_lab_center: Boolean = false,
    var language_uuid: Int = 0,
    var mobile: String = "",
    var modified_by: Int = 0,
    var modified_date: String = "",
    var nabh_logo: String = "",
    var name: String = "",
    var parent_facility_uuid: Int = 0,
    var phone: String = "",
    var revision: Int = 0,
    var speciality_uuid: Int = 0,
    var status: Boolean = false,
    var uuid: Int = 0
)