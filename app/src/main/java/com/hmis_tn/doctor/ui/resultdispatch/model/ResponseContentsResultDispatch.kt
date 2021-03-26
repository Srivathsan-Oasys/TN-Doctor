package com.hmis_tn.doctor.ui.resultdispatch.model

data class ResponseContentsResultDispatch(
    var approved_by: Int? = 0,
    var approved_details: ApprovedDetails? = ApprovedDetails(),
    var from_facility_uuid: Int? = 0,
    var patient_order_detail: PatientOrderDetail? = PatientOrderDetail(),
    var patient_order_detail_uuid: Int? = 0,
    var patient_order_test_detail_uuid: Int? = 0,
    var patient_order_uuid: Int? = 0,
    var patient_uuid: Int? = 0,
    var patient_work_order_uuid: Int? = 0,
    var to_facility_uuid: Int? = 0,
    var uuid: Int? = 0,
    var work_order_status_uuid: Int? = 0
)