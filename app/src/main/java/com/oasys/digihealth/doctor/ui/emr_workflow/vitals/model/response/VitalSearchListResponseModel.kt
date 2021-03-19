package com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.response

data class VitalSearchListResponseModel(
    var responseContents: VitalSearchList = VitalSearchList(),
    var message: String = "",
    var statusCode: Int = 0
)