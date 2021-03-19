package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.discharge_model

data class PrescriptionDetail(
    val administer_status_uuid: Int = 0,
    val appointment_uuid: Int = 0,
    val comments: Any = Any(),
    val consultation_uuid: Int = 0,
    val createdUser_name: String = "",
    val created_by: Int = 0,
    val created_date: String = "",
    val department_name: String = "",
    val department_uuid: Int = 0,
    val diagnosis_uuid: Int = 0,
    val dispense_status_uuid: Int = 0,
    val dispensed_by: Int = 0,
    val doctor_uuid: Int = 0,
    val encounter_doctor_uuid: Int = 0,
    val encounter_type_uuid: Int = 0,
    val encounter_uuid: Int = 0,
    val facility_name: String = "",
    val facility_uuid: Int = 0,
    val has_e_mar: Boolean = false,
    val injection_room_uuid: Int = 0,
    val is_active: Boolean = false,
    val is_digitally_signed: Boolean = false,
    val is_discharge_medication: Boolean = false,
    val modified_by: Int = 0,
    val modified_date: String = "",
    val notes: String = "",
    val other_diagnosis: Any = Any(),
    val patient_treatment_uuid: Int = 0,
    val patient_uuid: Int = 0,
    val prescription_date: String = "",
    val prescription_details: List<PrescriptionDetailX> = listOf(),
    val prescription_no: String = "",
    val prescription_status_uuid: Int = 0,
    val priscribedDoctor_name: String = "",
    val revision: Int = 0,
    val status: Boolean = false,
    val store_master_uuid: Int = 0,
    val tat_end_time: Any = Any(),
    val tat_start_time: Any = Any(),
    val treatment_kit_uuid: Int = 0,
    val uuid: Int = 0,
    val ward_master_uuid: Int = 0
)