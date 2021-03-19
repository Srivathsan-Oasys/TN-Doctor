package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model

data class ResponsePrescriptionFav(
    var code: Int? = 0,
    var message: String? = "",
    var responseContents: ResponsePresContents? = ResponsePresContents()
)