package com.oasys.digihealth.doctor.ui.quick_reg.model.session

data class ResponseContentsSession(
    var Evn_op_end_time: String? = "",
    var Evn_op_start_time: String? = "",
    var created_by: Int? = 0,
    var created_date: String? = "",
    var facility_uuid: Int? = 0,
    var is_active: Boolean? = false,
    var is_special_char_allowed: Any? = Any(),
    var max_login_name_length: Any? = Any(),
    var max_password_length: Any? = Any(),
    var min_login_name_length: Any? = Any(),
    var min_password_length: Any? = Any(),
    var modified_by: Int? = 0,
    var modified_date: String? = "",
    var mon_op_end_time: String? = "",
    var mon_op_start_time: String? = "",
    var op_extension_time: String? = "",
    var revision: Any? = Any(),
    var status: Boolean? = false,
    var uuid: Int? = 0
)