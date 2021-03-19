package com.oasys.digihealth.doctor.ui.tutorial.model

data class UserManualResponseContent(
    val activity_uuid: Int? = 0,
    val module_arr: List<UserModuleResponseContent?>? = listOf()

)