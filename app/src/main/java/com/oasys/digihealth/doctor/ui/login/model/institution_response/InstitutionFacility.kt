package com.oasys.digihealth.doctor.ui.login.model.institution_response

data class InstitutionFacility(
    val code: String? = "",
    val facility_type: FacilityType? = FacilityType(),
    val name: String? = "",
    val uuid: Int? = 0
)