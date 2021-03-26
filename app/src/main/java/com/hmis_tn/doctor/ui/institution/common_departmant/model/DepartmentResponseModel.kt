package com.hmis_tn.doctor.ui.institution.common_departmant.model

data class DepartmentResponseModel(
    var msg: String? = "",
    var responseContents: List<DepartmentResponseContent?>? = listOf(),
    var statusCode: Int? = 0
)