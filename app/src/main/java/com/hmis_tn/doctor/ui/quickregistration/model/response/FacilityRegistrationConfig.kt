package com.hmis_tn.doctor.ui.quickregistration.model.response

data class FacilityRegistrationConfig(
    var code: String = "",
    var facility_registration_config_type: FacilityRegistrationConfigType = FacilityRegistrationConfigType(),
    var facility_registration_config_type_uuid: Int = 0,
    var is_active: Boolean = false,
    var name: String = "",
    var status: Boolean = false,
    var uuid: Int = 0
)