package com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.request

data class TrasfferedRequestModel(
    var comments: String = "",
    var department_uuid: String = "",
    var encounter_type_uuid: String = "",
    var encounter_uuid: String = "",
    var facility_uuid: String = "",
    var patient_uuid: String = "",
    var transfer_comments: String = "",
    var transfer_date: String = "",
    var transfer_department_uuid: String = "",
    var transfer_facility_uuid: String = "",
    var transfer_reason_uuid: String = "",
    var transfer_type_uuid: Int = 0,
    var transfer_ward_uuid: String = ""
)