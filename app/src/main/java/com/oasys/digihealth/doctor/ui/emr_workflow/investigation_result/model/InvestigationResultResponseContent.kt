package com.oasys.digihealth.doctor.ui.emr_workflow.investigation_result.model

data class InvestigationResultResponseContent(
    val department: Any = Any(),
    val encounter_type: String = "",
    val encounter_type_uuid: Int = 0,
    val from_department_uuid: Int = 0,
    val order_request_date: String = "",
    val repsonse: List<Repsonse> = listOf(),
    val test_master: String = ""
)