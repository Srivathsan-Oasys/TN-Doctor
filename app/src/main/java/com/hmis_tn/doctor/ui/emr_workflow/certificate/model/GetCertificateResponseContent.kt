package com.hmis_tn.doctor.ui.emr_workflow.certificate.model

data class GetCertificateResponseContent(
    val certified_name: Any = Any(),
    val nt_is_active: Boolean = false,
    val nt_name: String = "",
    val nt_status: Boolean = false,
    val nt_uuid: Int = 0,
    val pc_approved_on: String = "",
    val pc_aproved_by: Int = 0,
    val pc_certified_by: Int = 0,
    val pc_data_template: String = "",
    val pc_department_uuid: Int = 0,
    val pc_doctor_uuid: Int = 0,
    val pc_facility_uuid: Int = 0,
    val pc_note_template_uuid: Int = 0,
    val pc_patient_uuid: Int = 0,
    val pc_status: Boolean = false,
    val pc_uuid: Int = 0,
    val u_first_name: String = "",
    val u_is_active: Boolean = false,
    val u_last_name: Any = Any(),
    val u_middle_name: Any = Any(),
    val u_status: Boolean = false,
    val u_uuid: Int = 0
)