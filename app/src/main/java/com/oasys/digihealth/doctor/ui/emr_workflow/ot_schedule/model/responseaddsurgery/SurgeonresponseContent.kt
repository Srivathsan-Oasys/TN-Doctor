package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery

data class SurgeonresponseContent(
    val facility: FacilityX? = FacilityX(),
    val first_name: String? = "",
    val last_name: Any? = Any(),
    val middle_name: Any? = Any(),
    val user_name: String? = "",
    val user_type: UserTypeX? = UserTypeX(),
    val username_facilityname: String? = "",
    val uuid: Int? = 0
)