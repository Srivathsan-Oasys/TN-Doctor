package com.oasys.digihealth.doctor.ui.emr_workflow.anesthesia_notes.model.add_consultations

data class ResponseContents(
    var approved_by: Int? = 0,
    var claim_number: String? = "",
    var claim_process_uuid: Int? = 0,
    var created_by: String? = "",
    var created_date: String? = "",
    var department_uuid: String? = "",
    var doctor_uuid: String? = "",
    var encounter_doctor_uuid: Int? = 0,
    var encounter_type_uuid: Int? = 0,
    var encounter_uuid: Int? = 0,
    var entry_status: Int? = 0,
    var facility_uuid: String? = "",
    var is_active: Boolean? = false,
    var is_latest: Boolean? = false,
    var last_consult_by: Int? = 0,
    var last_consult_date: Any? = Any(),
    var modified_by: String? = "",
    var modified_date: String? = "",
    var ot_register_uuid: Int? = 0,
    var patient_uuid: String? = "",
    var profile_type_uuid: Int? = 0,
    var profile_uuid: Int? = 0,
    var restrict_reason: String? = "",
    var revision: Int? = 0,
    var status: Boolean? = false,
    var uuid: Int? = 0,
    var visible_all_institutions: Boolean? = false,
    var visible_dept: Boolean? = false,
    var visible_institution: Boolean? = false,
    var visible_user: Boolean? = false,
    var visittype_uuid: Int? = 0,
    var ward_uuid: Int? = 0
)