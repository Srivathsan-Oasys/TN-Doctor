package com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model

data class BadRequestResponse(
    var code: Int? = 0,
    var existingDetails: ExistingDetails? = ExistingDetails(),
    var message: String? = ""
)