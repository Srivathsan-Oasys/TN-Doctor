package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.request

data class TreatmentKitPrescription(
    var item_master_uuid: Int? = 0,
    var drug_route_uuid: String? = "",
    var drug_frequency_uuid: String? = "",
    var duration: String? = "",
    var duration_period_uuid: String? = "",
    var drug_instruction_uuid: String? = "",
    var quantity: String? = ""
)



