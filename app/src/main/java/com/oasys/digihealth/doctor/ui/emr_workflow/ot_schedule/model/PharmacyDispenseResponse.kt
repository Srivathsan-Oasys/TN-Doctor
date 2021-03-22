package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model


data class PharmacyDispenseResponse(
    var statusCode: Int?,
    var message: String?,
    var responseContent: PatientDetail?,
    var totalRecords: Int?
)