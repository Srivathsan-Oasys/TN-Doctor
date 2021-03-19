package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model

import com.google.gson.annotations.SerializedName

data class ResPrescriptionData(
    @SerializedName("prescription_headers")
    var prescription_headers: PrescriptionHeaders? = null,
    @SerializedName("prescription_details")
    var prescription_details: List<PrescriptionDetails> = listOf()
)

data class PrescriptionHeaders(
    var context_uuid: Int? = 0,
    var context_activity_map_uuid: Int? = 0,
    var activity_uuid: Int? = 0,
    var display_order: Int? = 0
)

data class PrescriptionDetails(
    var uuid: Int? = 0,
    var prescription_no: String? = "",
    var prescription_date: String? = "",
    var prescription_status_uuid: Int? = 0,
    var dispense_status_uuid: Int? = 0,
    var dispensed_by: Int? = 0,
    var facility_uuid: Int? = 0,
    var department_uuid: Int? = 0,
    var ward_master_uuid: Int? = 0,
    var store_master_uuid: Int? = 0,
    var patient_uuid: Int? = 0,
    var encounter_uuid: Int? = 0,
    var encounter_doctor_uuid: Int? = 0,
    var treatment_kit_uuid: Int? = 0,
    var encounter_type_uuid: Int? = 0,
    var doctor_uuid: Long? = 0,
    var appointment_uuid: Int? = 0,
    var consultation_uuid: Int? = 0,
    var patient_treatment_uuid: Int? = 0,
    var diagnosis_uuid: Int? = 0,
    var notes: String? = "",
    var other_diagnosis: String? = "",
    var has_e_mar: Boolean? = false,
    var injection_room_uuid: Int? = 0,
    var administer_status_uuid: Int? = 0,
    var is_discharge_medication: Boolean? = false,
    var is_digitally_signed: Boolean? = false,
    var comments: String? = "",
    var revision: Int? = 0,
    var is_active: Boolean? = false,
    var status: Boolean? = false,
    var tat_start_time: String? = "",
    var tat_end_time: String? = "",
    var created_by: Long? = 0,
    var modified_by: Long? = 0,
    var created_date: String? = "",
    var modified_date: String? = "",
    var prescription_status: PrescriptionStatus? = null,
    var store_master: StoreMaster? = null,
    var prescription_details: List<PresDetails>? = null,
    var createdUser_name: String? = "",
    var priscribedDoctor_name: String? = "",
    var department_name: String? = "",
    var facility_name: String? = "",
    var encounter_type_name: String? = ""
)

data class PrescriptionStatus(
    var uuid: Int? = 0,
    var code: String? = "",
    var name: String? = ""
)

data class StoreMaster(
    var uuid: Int? = 0,
    var store_code: String? = "",
    var store_name: String? = ""
)

data class PresDetails(
    var uuid: Int? = 0,
    var prescription_uuid: Int? = 0,
    var item_master_uuid: Int? = 0,
    var generic_uuid: Int? = 0,
    var drug_route_uuid: Int? = 0,
    var drug_frequency_uuid: Int? = 0,
    var duration: Int? = 0,
    var duration_period_uuid: Int? = 0,
    var drug_instruction_uuid: Int? = 0,
    var prescribed_quantity: Int? = 0,
    var dispense_detail_status_uuid: Int? = 0,
    var administred_quantity: Int? = 0,
    var administer_detail_status_uuid: Int? = 0,
    var treatment_plan_drug_map_uuid: Int? = 0,
    var is_emar: Boolean? = false,
    var is_added_to_treatment_plan: Boolean? = false,
    var comments: String? = "",
    var revision: Int? = 0,
    var is_active: Boolean? = false,
    var status: Boolean? = false,
    var tat_start_time: String? = null,
    var tat_end_time: String? = null,
    var created_date: String? = "",
    var modified_date: String? = "",
    var drug_frequency: DrugFrequency? = null,
    var duration_period: DurationPeriod? = null,
    var drug_instruction: DrugInstruction? = null,
    var drug_route: DrugRoute? = null,
    var item_master: ItemMaster? = null
//    var stock_serial_items: StockSerialItems? = null,


)

data class DrugFrequency(
    var uuid: Int? = 0,
    var code: String? = "",
    var name: String? = "",
    var display: String? = ""
)

data class DurationPeriod(
    var uuid: Int? = 0,
    var code: String? = "",
    var name: String? = ""
)

data class DrugInstruction(
    var uuid: Int? = 0,
    var code: String? = "",
    var name: String? = ""
)

data class DrugRoute(
    var uuid: Int? = 0,
    var code: String? = "",
    var name: String? = ""
)

data class ItemMaster(
    var uuid: Int? = 0,
    var facility_uuid: Int? = 0,
    var code: String? = "",
    var name: String? = "",
    var strength: String? = "",
    var is_emar: Boolean? = false,
    var stock_item: StockItem? = null
)

data class StockItem(
    var uuid: Int? = 0,
    var facility_uuid: Int? = 0,
    var store_master_uuid: Int? = 0,
    var item_master_uuid: Int? = 0,
    var quantity: Int? = 0
)