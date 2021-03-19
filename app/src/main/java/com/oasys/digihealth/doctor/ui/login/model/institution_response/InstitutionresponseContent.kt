package com.oasys.digihealth.doctor.ui.login.model.institution_response


data class InstitutionresponseContent(
    val facility: InstitutionFacility? = InstitutionFacility(),
    val facility_uuid: Int? = 0,
    val user_uuid: Int? = 0,
    val uuid: Int? = 0,
    val department_uuid: Int = 0
)