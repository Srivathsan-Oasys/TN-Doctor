package com.oasys.digihealth.doctor.ui.emr_workflow.radiology_result.model

data class RadiologyTopResponseContent(
    val department: String = "",
    val encounter_type: String = "",
    val encounter_type_uuid: Int = 0,
    val from_department_uuid: Int = 0,
    val order_request_date: String = "",
    val repsonse: ArrayList<Repsonse> = ArrayList(),
    val test_master: String = ""
)