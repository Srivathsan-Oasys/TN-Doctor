package com.hmis_tn.doctor.ui.emr_workflow.diet.model

data class GetPreviousDietOrderResp(
    var statusCode: Int?,
    var status: String?,
    var responseContents: ResponseContents,
    var totalRecords: Int?
)

data class ResponseContents(
    var patient_diet_list: List<PatientDiet>?
)

data class PatientDiet(
    var created_date: String?,
    var department_name: String?,
    var department_uuid: Int?,
    var diet_order_status: String?,
    var diet_order_uuid: Int?,
    var doctor_name: String?,
    var doctor_title_name: String?,
    var doctor_uuid: Int?,
    var encounter_type_uuid: Int?,
    var patient_dietOrder_list: List<PatientDietOrder>?
) {
    var isExpanded = false
}

data class PatientDietOrder(
    var diet_category_name: String?,
    var diet_category_uuid: Int?,
    var diet_code: String?,
    var diet_frequency_name: String?,
    var diet_frequency_uuid: Int?,
    var diet_master_uuid: Int?,
    var diet_name: String?,
    var quantity: Int?
)
