package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.request

data class AdmissionUpdateRequestModel(
    var Id: Int? = 0,
    var admission_facility_uuid: String? = "",
    var admission_status_uuid: Int? = 0,
    var admitting_reason_uuid: Int? = 0,
    var comments: String? = "",
    var department_uuid: Int? = 0,
    var doctor_uuid: String? = "",
    var encounter_uuid: Int? = 0,
    var from_facility: String? = "",
    var patient_uuid: String? = "",
    var requested_date: String? = "",
    var ward_uuid: Int? = 0
)