package com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model

data class AmissionWardResponseContent(
    val department_name: String = "",
    val department_uuid: Int = 0,
    val facility_uuid: Int = 0,
    var ward_name: String = "",
    var ward_uuid: Int = 0
)