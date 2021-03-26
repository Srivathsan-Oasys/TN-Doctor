package com.hmis_tn.doctor.ui.resultdispatch.model

data class PatientOrderDetail(
    var auth_status_uuid: Int? = 0,
    var order_status_uuid: Int? = 0,
    var patient_order: PatientOrder? = PatientOrder(),
    var patient_order_test_details_uuid: Int? = 0,
    var test_master: TestMaster? = TestMaster(),
    var test_master_uuid: Int? = 0,
    var to_location_uuid: Int? = 0,
    var uuid: Int? = 0,
    var vw_patient_info: VwPatientInfo? = VwPatientInfo(),
    var vw_user_info: VwUserInfo? = VwUserInfo()
)