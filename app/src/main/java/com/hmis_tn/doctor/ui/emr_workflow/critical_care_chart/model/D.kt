package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.model

data class D(
    var ventilator_date: String?,
//    var ventilator_observed_value: String?,
    var ventilator_uuid: Int?,

    var abg_date: String?,
//    var abg_observed_value: String?,
    var abg_uuid: Int?,

    var monitor_date: String?,
//    var monitor_observed_value: String?,
    var monitor_uuid: Int?,

    var intake_date: String?,
//    var intake_observed_value: String?,
    var intake_uuid: Int?,

    var bp_date: String?,
//    var bp_observed_value: String?,
    var bp_uuid: Int?,

    var diabetes_date: String?,
//    var diabetes_observed_value: String?,
    var diabetes_uuid: Int?,

    var dialysis_date: String?,
//    var dialysis_observed_value: String?,
    var dialysis_uuid: Int?,

    var observed_value: String?,
    var ccc_code: String?,
    var ccc_desc: Any?,
    var ccc_name: String?,
    var ccc_uuid: Int?,
    var critical_care_type_code: String?,
    var critical_care_type_name: String?,
    var critical_care_type_uuid: Int?
)