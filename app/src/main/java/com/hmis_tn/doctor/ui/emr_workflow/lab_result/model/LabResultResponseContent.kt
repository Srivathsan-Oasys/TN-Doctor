package com.hmis_tn.doctor.ui.emr_workflow.lab_result.model

data class LabResultResponseContent(
    val department: String = "",
    val encounter_type: String = "",
    val encounter_type_uuid: Int = 0,
    val from_department_uuid: Int = 0,
    val order_request_date: String = "",
    val repsonse: List<Repsonse> = listOf(),
    val test_master: String = "",
    val test_master_uuid: Int? = 0,
    var isSelected: Boolean = false
)