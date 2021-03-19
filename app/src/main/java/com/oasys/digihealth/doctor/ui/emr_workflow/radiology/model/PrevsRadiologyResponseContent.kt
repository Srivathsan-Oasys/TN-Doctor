package com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model

data class PrevsRadiologyResponseContent(
    val created_date: String = "",
    val department_name: String = "",
    val department_uuid: Int = 0,
    val doctor_name: String = "",
    val doctor_uuid: Int = 0,
    val encounter_type_name: String = "",
    val encounter_type_uuid: Int = 0,
    val order_status: String = "",
    val pod_arr_result: List<PodArrResultX> = listOf()
)