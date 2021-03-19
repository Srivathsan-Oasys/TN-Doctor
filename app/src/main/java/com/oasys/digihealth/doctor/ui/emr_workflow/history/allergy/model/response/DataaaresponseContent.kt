package com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.response

data class DataaaresponseContent(
    val created_date: String? = "",
    val department_name: Any? = Any(),
    val department_uuid: Int? = 0,
    val doctor_name: String? = "",
    val doctor_uuid: Int? = 0,
    val order_status: String? = "",
    val pod_arr_result: List<PodArrResult?>? = listOf()
)