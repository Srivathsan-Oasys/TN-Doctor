package com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery.spinners

data class AnesthetistSpinnerresponseContent(
    val facility: Facility? = Facility(),
    val first_name: String? = "",
    val last_name: Any? = Any(),
    val middle_name: Any? = Any(),
    val user_name: Any? = Any(),
    val user_type: UserType? = UserType(),
    val username_facilityname: String? = "",
    val uuid: Int? = 0
)