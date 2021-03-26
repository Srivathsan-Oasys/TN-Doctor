package com.hmis_tn.doctor.ui.emr_workflow.certificate.model

data class CertificateRequestModel(
    var admission_status_uuid: Int = 0,
    var approved_on: String = "",
    var aproved_by: String = "",
    var certificate_status_uuid: Int = 0,
    var certified_by: String = "",
    var data_template: String = "",
    var department_uuid: String = "",
    var discharge_type_uuid: Int = 0,
    var doctor_uuid: String = "",
    var encounter_uuid: Int = 0,
    var facility_uuid: String = "",
    var note_template_uuid: Int = 0,
    var note_type_uuid: Int = 0,
    var patient_uuid: String = "",
    var released_by: String = "",
    var released_to_patient: Int = 0,
    var ward_master_uuid: Int = 0
)