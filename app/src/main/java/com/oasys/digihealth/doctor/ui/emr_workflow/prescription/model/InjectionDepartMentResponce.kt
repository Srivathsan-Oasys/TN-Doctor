package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model

data class InjectionDepartMentResponce(
    var responseContents: List<InjectionDepartment> = listOf(),
    var message: String = "",
    var status: Int = 0,
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)