package com.hmis_tn.doctor.ui.emr_workflow.history.surgery.model.response

data class Facility(
    val code: String? = "",
    val facility_type: FacilityType? = FacilityType(),
    val name: String? = "",
    val uuid: Int? = 0
)