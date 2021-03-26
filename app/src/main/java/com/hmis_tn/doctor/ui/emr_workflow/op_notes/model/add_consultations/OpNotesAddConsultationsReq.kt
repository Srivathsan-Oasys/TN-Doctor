package com.hmis_tn.doctor.ui.emr_workflow.op_notes.model.add_consultations

data class OpNotesAddConsultationsReq(
    var approved_by: Int? = 0,
    var claim_number: String? = "",
    var claim_process_uuid: Int? = 0,
    var department_uuid: String? = "",
    var doctor_uuid: String? = "",
    var encounter_doctor_uuid: Int? = 0,
    var encounter_type_uuid: Int? = 0,
    var encounter_uuid: Int? = 0,
    var entry_status: Int? = 0,
    var last_consult_by: Int? = 0,
    var last_consult_date: String? = "",
    var ot_register_uuid: Int? = 0,
    var patient_uuid: String? = "",
    var profile_type_uuid: Int? = 0,
    var profile_uuid: Int? = 0,
    var restrict_reason: String? = "",
    var visible_dept_uuid: Int? = 0,
    var visible_user_uuid: Int? = 0,
    var visittype_uuid: Int? = 0,
    var ward_uuid: Int? = 0
)