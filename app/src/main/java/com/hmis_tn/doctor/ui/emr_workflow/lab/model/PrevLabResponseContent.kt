package com.hmis_tn.doctor.ui.emr_workflow.lab.model

data class PrevLabResponseContent(
    val created_date: String = "",
    val department_name: String = "",
    val department_uuid: Int = 0,
    val doctor_name: String = "",
    val doctor_uuid: Int = 0,
    val encounter_type_name: String = "",
    val encounter_type_uuid: Int = 0,
    val order_status: String = "",
    val modified_date: String = "",
    var checkboxdeclardtatus: Boolean? = false,

    val pod_arr_result: List<PodArrResult> = listOf()
)

data class PrevLabResponse(
    var created_date: String = "",
    var pod_arr_result: PodArrResult? = null
)