package com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.model.request

data class ChiefComplaintRequestModel(
    var chief_complaint_duration: String = "",
    var chief_complaint_duration_period_uuid: String = "",
    var chief_complaint_uuid: Int = 0,
    var consultation_uuid: Int = 0,
    var encounter_type_uuid: Int = 0,
    var encounter_uuid: Int = 0,
    var patient_uuid: String = "",
    var revision: Int = 0,
    var encounter_doctor_uuid: Int = 0,
    var tat_end_time: String = "",
    var tat_start_time: String = "",
    var department_uuid: String = ""
)