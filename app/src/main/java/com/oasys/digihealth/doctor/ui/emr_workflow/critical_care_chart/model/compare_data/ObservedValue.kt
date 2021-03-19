package com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.model.compare_data

data class ObservedValue(
    var dList: List<D>?,
    var observed_date: String?

//    var ventilator_date: String?,
//    var abg_date: String?,
//    var monitor_date: String?,
//    var intake_date: String?,
//    var bp_date: String?,
//    var diabetes_date: String?,
//    var dialysis_date: String?
)