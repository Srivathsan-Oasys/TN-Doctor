package com.oasys.digihealth.doctor.ui.institute.model

data class UserDepartment(
    var department: Department? = Department(),
    var department_uuid: Int? = 0,
    var user_uuid: Int? = 0,
    var uuid: Int? = 0
)