package com.oasys.digihealth.doctor.ui.quickregistration.model

data class GetAllDepartmentRequest(
    var attributes: List<String> = listOf(),
    var casualtyBased: Boolean = false,
    var deptId: String = "",
    var genderId: Int = 0,
    var isClinical: Boolean = false,
    var pageNo: Int = 0,
    var paginationSize: Int = 0,
    var registrationBased: Boolean = false,
    var facilityBased: Boolean = false,
    var search: String = ""
)