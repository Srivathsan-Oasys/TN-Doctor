package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model

import com.google.gson.annotations.SerializedName

data class ResRadiologyData(
    @SerializedName("radiology_headers")
    var radiology_headers: RadiologyHeaders? = null,
    @SerializedName("radiology_details")
    var radiology_details: List<RadiologyDetails> = listOf()
)

data class RadiologyHeaders(
    var context_uuid: Int? = 0,
    var context_activity_map_uuid: Int? = 0,
    var activity_uuid: Int? = 0,
    var display_order: Int? = 0
)

data class RadiologyDetails(
    var uuid: Int? = 0,
    var patient_uuid: Long? = 0,
    var encounter_uuid: Long? = 0,
    var encounter_type_uuid: Int? = 0,
    var order_request_date: String? = "",
    var order_schedule_date: String? = "",
    var doctor_uuid: Long? = 0,
    var facility_uuid: Int? = 0,
    var department_uuid: Int? = 0,
    var order_to_location_uuid: Int? = 0,
    var created_date: String? = "",
    var modified_date: String? = "",
    @SerializedName("vw_patient_info")
    var vw_patient_info: VWPatientInfo? = null,
    @SerializedName("vw_user_info")
    var vw_user_info: VWUserInfo? = null,
    var to_location: String = "",
    @SerializedName("patient_order_test_details")
    var patient_order_test_details: List<PatientOrderTestDetails> = listOf(),
    var facility_name: String = "",
    var department_name: String = "",
    var encounter_type_name: String = ""
)