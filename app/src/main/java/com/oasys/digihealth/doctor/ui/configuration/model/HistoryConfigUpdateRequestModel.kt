package com.oasys.digihealth.doctor.ui.configuration.model

data class HistoryConfigUpdateRequestModel(

    var role_uuid: Int? = 0,
    var department_uuid: Int? = 0,
    var context_uuid: Int? = 0,
    var context_activity_map_uuid: Int? = 0,
    var activity_uuid: Int? = 0,
    var facility_uuid: Int? = 0,
    var user_uuid : Int?=0,
    var history_view_order:Int?=0


)