package com.hmis_tn.doctor.ui.emr_workflow.investigation.model

data class InvestigationPrevResponseContent(
    val created_date: String = "",
    val modified_date: String = "",
    val encounter_type_name: String = "",
    val department_name: String = "",
    val department_uuid: Int = 0,
    val doctor_name: String = "",
    val doctor_uuid: Int = 0,
    val order_status: String = "",
    var checkboxdeclardtatus: Boolean? = false,
    val pod_arr_result: List<PodArrResult> = listOf()
)