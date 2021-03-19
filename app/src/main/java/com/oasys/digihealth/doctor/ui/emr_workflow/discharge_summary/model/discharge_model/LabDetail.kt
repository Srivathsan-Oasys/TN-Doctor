package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.discharge_model


data class LabDetail(
    val created_date: String = "",
    val department_name: String = "",
    val department_uuid: Int = 0,
    val doctor_uuid: Int = 0,
    val encounter_type_name: String = "",
    val encounter_type_uuid: Int = 0,
    val encounter_uuid: Int = 0,
    val facility_name: String = "",
    val facility_uuid: Int = 0,
    val modified_date: String = "",
    val order_request_date: String = "",
    val order_schedule_date: String? = null,
    val order_to_location_uuid: Int = 0,
    val patient_order_test_details: List<PatientOrderTestDetailX> = listOf(),
    val patient_uuid: Int = 0,
    val to_location: ToLocation? = null,
    val uuid: Int = 0,
    val vw_patient_info: VwPatientInfoX = VwPatientInfoX(),
    val vw_user_info: VwUserInfoX = VwUserInfoX()
)