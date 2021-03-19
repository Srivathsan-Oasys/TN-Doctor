package com.oasys.digihealth.doctor.ui.institute.model

data class DepartmentResponseModel(
    var msg: String? = "",
    var req: DepartmentReq? = DepartmentReq(),
    var responseContents: List<DepartmentResponseContent?>? = listOf(),
    var statusCode: Int? = 0
)