package com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model

data class DoctorNameResponseContent(
    val first_name: String = "",
    val last_name: Any = Any(),
    val middle_name: Any = Any(),
    val role_uuid: Int = 0,
    val salutation_uuid: String = "",
    val title_name: String = "",
    val user_name: String = "",
    val uuid: Int = 0
)