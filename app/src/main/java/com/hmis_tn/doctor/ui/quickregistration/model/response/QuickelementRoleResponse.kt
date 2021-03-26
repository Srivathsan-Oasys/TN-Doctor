package com.hmis_tn.doctor.ui.quickregistration.model.response

data class QuickelementRoleResponse(
    var enabled: Boolean = false,
    var facility_registration_config: FacilityRegistrationConfig = FacilityRegistrationConfig(),
    var facility_registration_config_uuid: Int = 0,
    var facility_uuid: Int = 0,
    var is_active: Boolean = false,
    var status: Boolean = false,
    var uuid: Int = 0
)