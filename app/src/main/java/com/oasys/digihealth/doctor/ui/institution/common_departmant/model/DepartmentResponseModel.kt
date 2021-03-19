package com.oasys.digihealth.doctor.ui.institution.common_departmant.model

data class DepartmentResponseModel(
    var msg: String? = "",
    var responseContents: List<DepartmentResponseContent?>? = listOf(),
    var statusCode: Int? = 0
)