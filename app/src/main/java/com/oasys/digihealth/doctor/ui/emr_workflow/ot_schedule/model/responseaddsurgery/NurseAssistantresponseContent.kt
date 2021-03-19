package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery

data class NurseAssistantresponseContent(
    val facility: FacilityXX? = FacilityXX(),
    val first_name: String? = "",
    val last_name: Any? = Any(),
    val middle_name: Any? = Any(),
    val user_name: String? = "",
    val user_type: UserTypeXX? = UserTypeXX(),
    val username_facilityname: String? = "",
    val uuid: Int? = 0
)