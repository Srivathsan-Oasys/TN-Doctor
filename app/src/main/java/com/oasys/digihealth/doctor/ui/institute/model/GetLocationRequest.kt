package com.oasys.digihealth.doctor.ui.institute.model

data class GetLocationRequest(
    // var department_uuid: List<Int> = listOf(),
    var paginationSize: Int = 1000,
    var facility_uuid: Int = 0
)