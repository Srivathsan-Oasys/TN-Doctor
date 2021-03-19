package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model

data class PrescriptionResponseReq(
    var prescription_data: PrescriptionData? = PrescriptionData(),
    var prescription_details_data: List<PrescriptionDetailsData?>? = listOf()
)