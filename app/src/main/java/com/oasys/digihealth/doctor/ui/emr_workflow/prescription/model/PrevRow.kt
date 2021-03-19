package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model

data class PrevRow(
    var administer_status_uuid: Int? = 0,
    var appointment_uuid: Int? = 0,
    var comments: Any? = Any(),
    var consultation_uuid: Int? = 0,
    var createdUser_name: String? = "",
    var created_by: Int? = 0,
    var created_date: String? = "",
    var department_name: String? = "",
    var department_uuid: Int? = 0,
    var diagnosis_uuid: Int? = 0,
    var dispense_status_uuid: Int? = 0,
    var dispensed_by: Int? = 0,
    var doctor_uuid: Int? = 0,
    var encounter_doctor_uuid: Int? = 0,
    var encounter_type_uuid: Int? = 0,
    var encounter_uuid: Int? = 0,
    var facility_uuid: Int? = 0,
    var has_e_mar: Boolean? = false,
    var injection_room: InjectionData = InjectionData(),
    var injection_room_uuid: Int? = 0,
    var is_active: Boolean? = false,
    var is_discharge_medication: Boolean? = false,
    var modified_by: Int? = 0,
    var modified_date: String? = "",
    var notes: String? = "",
    var other_diagnosis: Any? = Any(),
    var patient_treatment_uuid: Int? = 0,
    var patient_uuid: Int? = 0,
    var prescription_date: String? = "",
    var prescription_details: List<PrescriptionDetail?>? = listOf(),
    var prescription_no: String? = "",
    var prescription_status: PrescriptionStatus? = PrescriptionStatus(),
    var prescription_status_uuid: Int? = 0,
    var priscribedDoctor_name: String? = "",
    var revision: Int? = 0,
    var status: Boolean? = false,
    var store_master: StoreMaster? = StoreMaster(),
    var store_master_uuid: Int? = 0,
    var tat_end_time: Any? = Any(),
    var tat_start_time: Any? = Any(),
    var treatment_kit_uuid: Int? = 0,
    var uuid: Int? = 0,
    var ward_master_uuid: Int? = 0
)

data class InjectionData(
    var store_code: String = "",
    var store_name: String = "",
    var uuid: Int = 0

)