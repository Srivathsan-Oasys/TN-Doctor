package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.response

data class AdmissionSaveresponseContents(
    val admission_facility_uuid: String? = "",
    val admission_status_uuid: Int? = 0,
    val admitting_reason_uuid: Int? = 0,
    val created_by: String? = "",
    val created_date: String? = "",
    val department_uuid: String? = "",
    val doctor_uuid: String? = "",
    val encounter_uuid: Int? = 0,
    val from_facility: String? = "",
    val is_active: Boolean? = false,
    val is_refer: String? = "",
    val is_transfer: String? = "",
    val modified_by: String? = "",
    val modified_date: String? = "",
    val patient_uuid: String? = "",
    val status: Boolean? = false,
    val tr_re_admission_uuid: String? = "",
    val uuid: Int? = 0,
    val ward_uuid: String? = ""
)