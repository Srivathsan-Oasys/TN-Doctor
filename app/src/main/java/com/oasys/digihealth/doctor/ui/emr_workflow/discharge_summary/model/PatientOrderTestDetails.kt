package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model

import com.google.gson.annotations.SerializedName

data class PatientOrderTestDetails(
    var uuid: Int? = 0,
    var patient_uuid: Long? = 0,
    var patient_order_uuid: Int? = 0,
    var test_master_uuid: Int? = 0,
    var order_priority_uuid: Int? = 0,
    var order_status_uuid: Int? = 0,
    var sample_collection_date: String? = null,
    var is_nurse_collected: Boolean? = false,
    @SerializedName("test_master")
    var test_master: TestMaster? = null,
    @SerializedName("order_status")
    var order_status: OrderStatus? = null,
    @SerializedName("order_priority")
    var order_priority: OrderStatus? = null
)

data class TestMaster(
    var uuid: Int? = 0,
    var code: String? = "",
    var name: String? = "",
    var description: String? = "",
    var sample_type_uuid: Int? = 0,
    var value_type_uuid: Int? = 0,
    var department_uuid: Int? = 0,
    var sub_department_uuid: Int? = 0,
    var is_active: Boolean? = false
)

//Both are same so used common class for status and priority
data class OrderStatus(
    var uuid: Int? = 0,
    var code: String? = "",
    var name: String? = ""
)

data class VWPatientInfo(
    var uuid: Int? = 0,
    var uhid: Long? = 0,
    var pattitle: String? = "",
    var mobile: String? = "",
    var ageperiod: String? = "",
    var gender_uuid: Int? = 0,
    var first_name: String? = "",
    var middle_name: String? = "",
    var last_name: String? = "",
    var age: Double? = 0.0
)

data class VWUserInfo(
    var uuid: Int? = 0,
    var title_name: String? = "",
    var first_name: String? = "",
    var middle_name: String? = "",
    var last_name: String? = "",
    var gender_uuid: Int? = 0,
    var gender_name: String? = "",
    var age: Double? = 0.0
)