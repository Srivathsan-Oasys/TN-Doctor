package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.favouriteEdit.updaterequest

data class UpdatePrescriptionRequest(
    var chief_complaint_id: String? = "",
    var department_id: String? = "",
    var drug_duration: Int? = 0,
    var drug_frequency_id: Int? = 0,
    var drug_instruction_id: Int? = 0,
    var drug_period_id: Int? = 0,
    var drug_route_id: String? = "",
    var favourite_display_order: Int? = 0,
    var favourite_id: Int? = 0,
    var is_active: Boolean? = true,
    var vital_master_id: String? = ""
)