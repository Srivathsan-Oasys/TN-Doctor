package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model

import com.google.gson.annotations.SerializedName

data class ResVitalsData(
    @SerializedName("vital_headers")
    var vital_headers: VitalHeaders? = null,
    @SerializedName("vital_details")
    var vital_details: List<VitalDetails> = listOf()
)

data class VitalHeaders(
    var context_uuid: Int? = 0,
    var context_activity_map_uuid: Int? = 0,
    var activity_uuid: Int? = 0,
    var display_order: Int? = 0
)

data class VitalDetails(
    var patient_vital_uuid: Int? = 0,
    var patient_uuid: Long? = 0,
    var created_date: String? = "",
    var doctor_uuid: Long? = 0,
    var doctor_title: String? = "",
    var doctor_firstname: String? = "",
    var doctor_middlename: String? = "",
    var doctor_lastlename: String? = "",
    var department_name: String? = "",
    var institution_uuid: Int? = 0,
    var institution_name: String? = "",
    var encounter_uuid: Long? = 0,
    var encounter_type_uuid: Int? = 0,
    var encounter_type_code: String? = "",
    var encounter_type_name: String? = "",
    var PV_list: List<PVList>? = listOf()
)

data class PVList(
    var patient_vital_uuid: Int? = 0,
    var vital_master_uuid: Int? = 0,
    var vital_value: String? = "",
    var vital_performed_date: String? = "",
    var vital_value_type_uuid: Int? = 0,
    var vital_type_uuid: Int? = 0,
    var vital_name: String? = "",
    var uom_code: String? = "",
    var uom_name: String? = ""
)