package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response

data class RadiologyDetail(
    val radiology_code: String? = "",
    val radiology_description: String? = "",
    val radiology_id: Int? = 0,
    val radiology_name: String? = "",

    var selectTypeUUID: Int = 0,
    var selectTypeName: String? = null,
    var selectRouteID: Int = 0,
    var selecteFrequencyID: Int = 0,
    var selectInvestID: Int = 0,
    var selectToLocationUUID: Int = 0,
    var selectedLocationName: String = "",
    var selectToTestMethodUUID: Int = 0,
    var selectToTestMethodName: String? = null,

    var order_priority_uuid: Int? = 0,
    var order_to_location_uuid: Int? = 0
)